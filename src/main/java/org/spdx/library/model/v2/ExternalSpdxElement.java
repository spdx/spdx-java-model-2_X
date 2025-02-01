/**
 * Copyright (c) 2019 Source Auditor Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.spdx.library.model.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;

import javax.annotation.Nullable;

import org.spdx.core.CoreModelObject;
import org.spdx.core.DefaultModelStore;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.IndividualUriValue;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.SimpleUriValue;
import org.spdx.storage.IModelStore;
import org.spdx.storage.compatv2.CompatibleModelStoreWrapper;


/**
 * This is an SPDX element which is in an external document.
 *
 * Note that the ExternalSpdxElement is implemented differently than the previous major version of the
 * SPDX libraries.
 * 
 * The constructor now takes the documentUri and ID of the target external SPDX element NOT
 * the documentUri of the referencing document and the ID is the SPDX ID, not the documentRef version.
 * 
 * @author Gary O'Neall
 */
public class ExternalSpdxElement extends SpdxElement implements IndividualUriValue {
	
	// Note: The documentUri and ID must be specified
	
	public ExternalSpdxElement(String documentUri, String id) throws InvalidSPDXAnalysisException {
		this(DefaultModelStore.getDefaultModelStore(), 
				checkConvertDocumentUri(documentUri, id, DefaultModelStore.getDefaultModelStore()), 
				checkConvertId(id), DefaultModelStore.getDefaultCopyManager());
	}
	
	/**
	 * @param modelStore Store to store THIS reference to an external SPDX element
	 * @param documentUri documentURI for representing the external document
	 * @param id ID of the external SPDX element
	 * @param create this parameter is ignored since it is external
	 * @throws InvalidSPDXAnalysisException
	 */
	public ExternalSpdxElement(IModelStore modelStore, String documentUri, String id, 
			@Nullable IModelCopyManager copyManager, boolean create)
			throws InvalidSPDXAnalysisException {
		super(modelStore, checkConvertDocumentUri(documentUri, id, modelStore), checkConvertId(id), copyManager, true);
	}

	/**
	 * @param modelStore Store to store THIS reference to an external SPDX element
	 * @param documentUri documentURI for representing the external document
	 * @param id ID of the external SPDX element
	 * @throws InvalidSPDXAnalysisException
	 */
	public ExternalSpdxElement(IModelStore modelStore, String documentUri, String id, 
			@Nullable IModelCopyManager copyManager)
			throws InvalidSPDXAnalysisException {
		super(modelStore, checkConvertDocumentUri(documentUri, id, modelStore), checkConvertId(id), copyManager, true);
	}
	
	/**
	 * @param documentUri document URI passed into the constructor
	 * @param id passed into the constructor
	 * @return if id contains a colon (':'), look up the external document URI, otherwise return the documentUri parameter
	 * @throws InvalidSPDXAnalysisException 
	 */
	private static String checkConvertDocumentUri(String documentUri,
			String id, IModelStore modelStore) throws InvalidSPDXAnalysisException {
		if (id.contains(":")) {
			String externalUri = externalSpdxElementIdToURI(id, modelStore, documentUri, null);
			return externalUri.substring(0, externalUri.indexOf('#'));
		} else {
			return documentUri;
		}
	}

	/**
	 * @param id id passed into a constructor
	 * @return if id contains a colon ':', remove the document reference from the value otherwise just return the id
	 */
	private static String checkConvertId(String id) {
		if (id.contains(":")) {
			return id.substring(id.lastIndexOf(':')+1);
		} else {
			return id;
		}
	}

	/**
	 * @param documentReferencingExternal document containing the external reference
	 * @return external document ID for the external reference
	 * @throws InvalidSPDXAnalysisException
	 */
	public String getExternalDocumentId(SpdxDocument documentReferencingExternal) throws InvalidSPDXAnalysisException {
		Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_SPDX_ELEMENT_URI_PATTERN.matcher(getObjectUri());
		if (!matcher.matches()) {
			throw new InvalidSPDXAnalysisException("Invalid URI format: "+getObjectUri()+".  Expects namespace#SPDXRef-XXX");
		}
		Optional<ExternalDocumentRef> externalDocRef = ExternalDocumentRef.getExternalDocRefByDocNamespace(documentReferencingExternal.getModelStore(),
				documentReferencingExternal.getDocumentUri(), matcher.group(1), documentReferencingExternal.getCopyManager(), 
				documentReferencingExternal.getSpecVersion());
		if (!externalDocRef.isPresent()) {
			logger.error("Could not find or create the external document reference for document namespace "+ matcher.group(1));
			throw new InvalidSPDXAnalysisException("Could not find or create the external document reference for document namespace "+ matcher.group(1));
		}
		return externalDocRef.get().getId();
	}
	
	@Override
	public boolean isExternal() {
		return true;
	}
	
	/**
	 * @return element ID used in the external document
	 * @throws InvalidSPDXAnalysisException
	 */
	public String getExternalElementId() throws InvalidSPDXAnalysisException {
		return this.getId();
	}

	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#getType()
	 */
	@Override
	public String getType() {
		return SpdxConstantsCompatV2.CLASS_EXTERNAL_SPDX_ELEMENT;
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.SpdxElement#_verify(java.util.List)
	 */
	@Override
	protected List<String> _verify(Set<String> verifiedIds, String specVersion) {
		// we don't want to call super.verify since we really don't require those fields
		List<String> retval = new ArrayList<>();
		String objectUri = getObjectUri();
		Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_SPDX_ELEMENT_URI_PATTERN.matcher(objectUri);
		if (!matcher.matches()) {				
			retval.add("Invalid objectUri format for an external document reference.  Must be of the form "+SpdxConstantsCompatV2.EXTERNAL_ELEMENT_REF_PATTERN.pattern());
		}
		// TODO: See if there is a way to verify the referring SPDX document includes this referenced document URI
		return retval;
	}
	
	/**
	 * @param documentReferencingExternal document containing the external reference
	 * @return the ID used for referencing this external element within the documentReferencingExternal
	 * of the form DocRef-XXX:SpdxRef-YYY
	 * @throws InvalidSPDXAnalysisException on error in SPDX parsing
	 */
	public String referenceElementId(SpdxDocument documentReferencingExternal) throws InvalidSPDXAnalysisException {
		return uriToExternalSpdxElementReference(getObjectUri(), documentReferencingExternal.getModelStore(),
				documentReferencingExternal.getDocumentUri(), documentReferencingExternal.getCopyManager(),
				documentReferencingExternal.getSpecVersion());
	}
	
	/**
	 * @param externalSpdxElementId ID which is referencing the external document using the docRef:id pattern
	 * @param stModelStore model store
	 * @param stDocumentUri Document URI which is referencing the external document
	 * @param copyManager
	 * @return The URI associated with the external SPDX element with the ID externalSpdxElementId
	 * @throws InvalidSPDXAnalysisException
	 */
	public static String externalSpdxElementIdToURI(String externalSpdxElementId,
			IModelStore stModelStore, String stDocumentUri, IModelCopyManager copyManager) throws InvalidSPDXAnalysisException {
		Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_ELEMENT_REF_PATTERN.matcher(externalSpdxElementId);
		if (!matcher.matches()) {
			logger.error("Invalid objectUri format for an external document reference.  Must be of the form ExternalSPDXRef:SPDXID");
			throw new InvalidSPDXAnalysisException("Invalid objectUri format for an external document reference.  Must be of the form ExternalSPDXRef:SPDXID");
		}
		String externalDocumentUri;
		externalDocumentUri = externalDocumentIdToNamespace(matcher.group(1), stModelStore, stDocumentUri, copyManager);
		if (externalDocumentUri.endsWith("#")) {
			return externalDocumentUri + matcher.group(2);
		} else {
			return externalDocumentUri + "#" + matcher.group(2);
		}
	}
	
	/**
	 * Convert a URI to an ID for an External SPDX Element
	 * @param uri URI with the external document namespace and the external SPDX ref in the form namespace#SPDXRef-[NUM]
	 * @param stModelStore
	 * @param stDocumentUri documentUri for the document referencing the external document
	 * @param copyManager if non-null, create the external Doc ref if it is not a property of the SPDX Document
	 * @param specVersion version of the spec used for this external SPDX element
	 * @return internal reference for the external SPDX element ID in the form DocumentRef-XX:SPDXRef-YY
	 * @throws InvalidSPDXAnalysisException
	 */
	public static String uriToExternalSpdxElementReference(String uri,
			IModelStore stModelStore, String stDocumentUri, IModelCopyManager copyManager, String specVersion) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(uri, "URI can not be null");
		Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_SPDX_ELEMENT_URI_PATTERN.matcher(uri);
		if (!matcher.matches()) {
			throw new InvalidSPDXAnalysisException("Invalid URI format: "+uri+".  Expects namespace#SPDXRef-XXX");
		}
		Optional<ExternalDocumentRef> externalDocRef = ExternalDocumentRef.getExternalDocRefByDocNamespace(stModelStore, stDocumentUri, 
				matcher.group(1), copyManager, specVersion);
		if (!externalDocRef.isPresent()) {
			logger.error("Could not find or create the external document reference for document namespace "+ matcher.group(1));
			throw new InvalidSPDXAnalysisException("Could not find or create the external document reference for document namespace "+ matcher.group(1));
		}
		return externalDocRef.get().getId() + ":" + matcher.group(2);
	}

	
	public static String externalDocumentIdToNamespace(String externalDocumentId,
			IModelStore stModelStore, String stDocumentUri, IModelCopyManager copyManager) throws InvalidSPDXAnalysisException {
		Optional<Object> retval = stModelStore.getValue(CompatibleModelStoreWrapper.documentUriIdToUri(stDocumentUri, externalDocumentId, false),
				SpdxConstantsCompatV2.PROP_EXTERNAL_SPDX_DOCUMENT);
		if (!retval.isPresent()) {
			throw new InvalidSPDXAnalysisException("No external document reference exists for document ID "+externalDocumentId);
		}
		if (!(retval.get() instanceof IndividualUriValue)) {
			logger.error("Invalid type returned for external document.  Expected IndividualValue, actual "+retval.get().getClass().toString());
			throw new InvalidSPDXAnalysisException("Invalid type returned for external document.");
		}
		return ((IndividualUriValue)retval.get()).getIndividualURI();
	}
	
	@Override
	public boolean equivalent(CoreModelObject compare) {
		if (!(compare instanceof ExternalSpdxElement)) {
			return false;
		}
		return getObjectUri().equals(compare.getObjectUri());
	}

	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#equivalent(org.spdx.library.model.compat.v2.compat.v2.ModelObject, boolean)
	 */
	@Override
	public boolean equivalent(CoreModelObject compare, boolean ignoreRelatedElements) throws InvalidSPDXAnalysisException {
		return equivalent(compare);
	}
	
	@Override
	public String getIndividualURI() {
		return getObjectUri();
	}
	
	@Override
	public boolean addAnnotation(Annotation annotation) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can not add annotations to an ExternalSpdxElement.  "
				+ "These changes must be done to the local SPDX element in the document which defines the SPDX element.");
	}
	
	@Override
	public boolean addRelationship(Relationship relationship) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can not add relationships to an ExternalSpdxElement.  "
				+ "These changes must be done to the local SPDX element in the document which defines the SPDX element.");
	}
	
	@Override
	public void setComment(String comment) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can not set comment on an ExternalSpdxElement.  "
				+ "These changes must be done to the local SPDX element in the document which defines the SPDX element.");
	}
	
	@Override
	public ExternalSpdxElement setName(String name) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can not set the name on an ExternalSpdxElement.  "
				+ "These changes must be done to the local SPDX element in the document which defines the SPDX element.");
	}
	
	/**
	 * @return Annotations
	 * @throws InvalidSPDXAnalysisException
	 */
	@Override
	public Collection<Annotation> getAnnotations() throws InvalidSPDXAnalysisException {
		return new ArrayList<Annotation>();
	}
	
	/**
	 * Clears and resets the annotations collection to the parameter
	 * @param annotations
	 * @return this to chain setters
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Override
	public SpdxElement setAnnotations(Collection<Annotation> annotations) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can not set annotations to an ExternalSpdxElement.  "
				+ "These changes must be done to the local SPDX element in the document which defines the SPDX element.");
	}
	

	/**
	 * Remove an annotation
	 * @param annotation
	 * @return
	 * @throws InvalidSPDXAnalysisException
	 */
	@Override
	public boolean removeAnnotation(Annotation annotation) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can remove set annotations to an ExternalSpdxElement.  "
				+ "These changes must be done to the local SPDX element in the document which defines the SPDX element.");
	}
	
	/**
	 * @return Relationships
	 * @throws InvalidSPDXAnalysisException
	 */
	@Override
	public Collection<Relationship> getRelationships() throws InvalidSPDXAnalysisException {
		return new ArrayList<Relationship>();
	}
	
	/**
	 * clear and reset the relationships to the parameter relationship
	 * @param relationships
	 * @return this to chain sets
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Override
	public SpdxElement setRelationships(Collection<Relationship> relationships) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can not set relationships on an ExternalSpdxElement.  "
				+ "These changes must be done to the local SPDX element in the document which defines the SPDX element.");
	}
	
	/**
	 * Remove a relationship
	 * @param relationship
	 * @return
	 * @throws InvalidSPDXAnalysisException
	 */
	@Override
	public boolean removeRelationship(Relationship relationship) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can not remove relationships on an ExternalSpdxElement.  "
				+ "These changes must be done to the local SPDX element in the document which defines the SPDX element.");
	}
	
	/**
	 * @return the comment
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Override
	public Optional<String> getComment() throws InvalidSPDXAnalysisException {
		return Optional.empty();
	}
	
	/**
	 * @return the name
	 */
	@Override
	public Optional<String> getName() throws InvalidSPDXAnalysisException {
		return Optional.empty();
	}
	
	@Override
	public boolean equals(Object comp) {
		return SimpleUriValue.isIndividualUriValueEquals(this, comp);
	}

	@Override
	public int hashCode() {
		return SimpleUriValue.getIndividualUriValueHash(this);
	}
}
