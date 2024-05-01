/**
 * Copyright (c) 2020 Source Auditor Inc.
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
package org.spdx.library.model.v2.license;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;

import javax.annotation.Nullable;

import org.spdx.core.CoreModelObject;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.IndividualUriValue;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.SimpleUriValue;
import org.spdx.library.model.v2.ExternalDocumentRef;
import org.spdx.library.model.v2.ExternalSpdxElement;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;
import org.spdx.storage.IModelStore;

/**
 * @author Gary O'Neall
 * 
 * This class represents an ExtractedLicenseInfo which is stored in an external SPDX document.
 * 
 * Note that the actual properties for this ExtractedLicenseInfo is in an external document so
 * it is not accessible through this class.
 * 
 * The set methods will cause an exception.
 * 
 * The <code>getExtractedText()</code> will return text that indicates the actual license text
 * is in an external document.
 * 
 * NOTE that this implementation is different from the previous major version of the SPDX Library.
 * The documentUri is the documentUri where the extractedLicenseRef can be found.
 * The ID is the ID for the LicenseRef in the external document.
 *
 */
public class ExternalExtractedLicenseInfo extends AbstractExtractedLicenseInfo implements IndividualUriValue {
	
	// Note: the constructors which use the default model store are not allowed
	
	/**
	 * @param modelStore
	 * @param documentUri
	 * @param objectUri
	 * @param copyManager
	 * @param create
	 * @throws InvalidSPDXAnalysisException
	 */
	public ExternalExtractedLicenseInfo(IModelStore modelStore, String documentUri, String id, 
			@Nullable IModelCopyManager copyManager, boolean create)
			throws InvalidSPDXAnalysisException {
		super(modelStore, documentUri, id, copyManager, create);	
	}
	
	/**
	 * @return external document ID for the external reference
	 * @throws InvalidSPDXAnalysisException
	 */
	public String getExternalDocumentId() throws InvalidSPDXAnalysisException {
		return this.getDocumentUri();
	}
	
	/**
	 * @return element ID used in the external document
	 * @throws InvalidSPDXAnalysisException
	 */
	public String getExternalLicenseRef() throws InvalidSPDXAnalysisException {
		return this.getId();
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#getType()
	 */
	@Override
	public String getType() {
		return SpdxConstantsCompatV2.CLASS_EXTERNAL_EXTRACTED_LICENSE;
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.SpdxElement#_verify(java.util.List)
	 */
	@Override
	protected List<String> _verify(Set<String> verifiedIds, String specVersion) {
		// we don't want to call super.verify since we really don't require those fields
		List<String> retval = new ArrayList<>();
		String id = this.getId();
		Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_EXTRACTED_LICENSE_PATTERN.matcher(id);
		if (!matcher.matches()) {				
			retval.add("Invalid objectUri format for an external document reference.  Must be of the form "+SpdxConstantsCompatV2.EXTERNAL_ELEMENT_REF_PATTERN.pattern());
		} else {
			try {
				ExternalSpdxElement.externalDocumentIdToNamespace(matcher.group(1), getModelStore(), getDocumentUri(), getCopyManager());
			} catch (InvalidSPDXAnalysisException e) {
				retval.add(e.getMessage());
			}
		}
		return retval;
	}
	
	/**
	 * @return the URI associated with this external SPDX Extracted License
	 * @throws InvalidSPDXAnalysisException
	 */
	public String getExternalExtractedLicenseURI() throws InvalidSPDXAnalysisException {
		return getDocumentUri() + "#" + getId();
	}
	
	/**
	 * @param externalExtractedLicenseId id of the form documentRef:id
	 * @param stModelStore model store
	 * @param stDocumentUri document URI for the document which is referring to the external license
	 * @param copyManager
	 * @return The URI associated with the external LicenseRef with the ID externalLicenseRefId
	 * @throws InvalidSPDXAnalysisException
	 */
	public static String externalExtractedLicenseIdToURI(String externalExtractedLicenseId,
			IModelStore stModelStore, String stDocumentUri, IModelCopyManager copyManager) throws InvalidSPDXAnalysisException {
		Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_EXTRACTED_LICENSE_PATTERN.matcher(externalExtractedLicenseId);
		if (!matcher.matches()) {
			logger.error("Invalid objectUri format for an external document reference.  Must be of the form ExternalSPDXRef:LicenseRef-XXX");
			throw new InvalidSPDXAnalysisException("Invalid objectUri format for an external document reference.  Must be of the form ExternalSPDXRef:LicenseRef-XXX");
		}
		String externalDocumentUri;
		externalDocumentUri = ExternalSpdxElement.externalDocumentIdToNamespace(matcher.group(1), stModelStore, stDocumentUri, copyManager);
		if (externalDocumentUri.endsWith("#")) {
			return externalDocumentUri + matcher.group(2);
		} else {
			return externalDocumentUri + "#" + matcher.group(2);
		}
	}
	
	/**
	 * Convert a URI to an ID for an External Extracted License
	 * @param uri URI with the external document namespace and the external Extracted License in the form namespace#LicenseRef-XXXX
	 * @param stModelStore
	 * @param stDocumentUri
	 * @param copyManager if non-null, create the external doc ref if it is not already in the ModelStore
	 * @return external SPDX element ID in the form DocumentRef-XX:LicenseRef-XXXX
	 * @param specVersion - version of the SPDX spec the object complies with
	 * @throws InvalidSPDXAnalysisException
	 */
	public static String uriToExternalExtractedLicenseId(String uri,
			IModelStore stModelStore, String stDocumentUri, IModelCopyManager copyManager, 
			String specVersion) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(uri, "URI can not be null");
		Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_EXTRACTED_LICENSE_URI_PATTERN.matcher(uri);
		if (!matcher.matches()) {
			throw new InvalidSPDXAnalysisException("Invalid URI format: "+uri+".  Expects namespace#LicenseRef-XXXX");
		}
		Optional<ExternalDocumentRef> externalDocRef = ExternalDocumentRef.getExternalDocRefByDocNamespace(stModelStore, stDocumentUri, 
				matcher.group(1), copyManager, specVersion);
		if (!externalDocRef.isPresent()) {
			logger.error("Could not find or create the external document reference for document namespace "+ matcher.group(1));
			throw new InvalidSPDXAnalysisException("Could not find or create the external document reference for document namespace "+ matcher.group(1));
		}
		return externalDocRef.get().getId() + ":" + matcher.group(2);
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#equivalent(org.spdx.library.model.compat.v2.compat.v2.ModelObject)
	 */
	@Override
	public boolean equivalent(CoreModelObject compare) {
		if (!(compare instanceof ExternalExtractedLicenseInfo)) {
			return false;
		}
		return getId().equals(((ExternalExtractedLicenseInfo)compare).getId());
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#equivalent(org.spdx.library.model.compat.v2.compat.v2.ModelObject, boolean)
	 */
	@Override
	public boolean equivalent(CoreModelObject compare, boolean ignoreRelatedElements) throws InvalidSPDXAnalysisException {
		return equivalent(compare);
	}

	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.IndividualUriValue#getIndividualURI()
	 */
	@Override
	public String getIndividualURI() {
		try {
			return getExternalExtractedLicenseURI();
		} catch (InvalidSPDXAnalysisException e) {
			logger.error("Error getting external LicenseRef URI",e);
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.license.AbstractExtractedLicenseInfo#getExtractedText()
	 */
	@Override
	public String getExtractedText() throws InvalidSPDXAnalysisException {
		return "The text for this license can be found in the external document "
				+ getExternalDocumentId()
				+ " license Ref "
				+ getExternalLicenseRef()
				+ ".";
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.license.SimpleLicensingInfo#getComment()
	 */
	@Override
	public String getComment() throws InvalidSPDXAnalysisException {
		return "This is an external LicenseRef - see the document containing the license for any comments";
	}
	
	/**
	 * @param comment the comment to set
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Override
	public void setComment(String comment) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can not set comment for an external LicenseRef.  "
				+ "Changes to the license need to be made within the document containing the license.");
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.license.SimpleLicensingInfo#getSeeAlso()
	 */
	@Override
	public Collection<String> getSeeAlso() throws InvalidSPDXAnalysisException {
		return new ArrayList<>();
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.license.SimpleLicensingInfo#setSeeAlso(java.util.Collection)
	 */
	@Override
	public void setSeeAlso(Collection<String> seeAlsoUrl) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can not set seeAlso for an external LicenseRef.  "
				+ "Changes to the license need to be made within the document containing the license.");
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.license.SimpleLicensingInfo#getName()
	 */
	@Override
	public String getName() throws InvalidSPDXAnalysisException {
		return "";
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.license.SimpleLicensingInfo#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException("Can not set name for an external LicenseRef.  "
				+ "Changes to the license need to be made within the document containing the license.");
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