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

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spdx.core.CoreModelObject;
import org.spdx.core.DefaultModelStore;
import org.spdx.core.DefaultStoreNotInitialized;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.IndividualUriValue;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelObjectHelper;
import org.spdx.core.SpdxInvalidTypeException;
import org.spdx.core.TypedValue;
import org.spdx.library.model.v2.enumerations.AnnotationType;
import org.spdx.library.model.v2.enumerations.ChecksumAlgorithm;
import org.spdx.library.model.v2.enumerations.ReferenceCategory;
import org.spdx.library.model.v2.enumerations.RelationshipType;
import org.spdx.library.model.v2.license.AnyLicenseInfo;
import org.spdx.library.model.v2.license.ConjunctiveLicenseSet;
import org.spdx.library.model.v2.license.CrossRef.CrossRefBuilder;
import org.spdx.library.model.v2.license.DisjunctiveLicenseSet;
import org.spdx.library.model.v2.license.SpdxNoAssertionLicense;
import org.spdx.library.model.v2.license.SpdxNoneLicense;
import org.spdx.library.model.v2.pointer.ByteOffsetPointer;
import org.spdx.library.model.v2.pointer.LineCharPointer;
import org.spdx.library.model.v2.pointer.SinglePointer;
import org.spdx.library.model.v2.pointer.StartEndPointer;
import org.spdx.storage.CompatibleModelStoreWrapper;
import org.spdx.storage.IModelStore;
import org.spdx.storage.IModelStore.IModelStoreLock;
import org.spdx.storage.IModelStore.IdType;
import org.spdx.storage.PropertyDescriptor;

/**
 * @author Gary O'Neall
 * 
 * Superclass for all SPDX spec version 2 model objects
 * 
 * Provides the primary interface to the storage class that access and stores the data for 
 * the model objects.
 * 
 * This class includes several helper methods to manage the storage and retrieval of properties.
 * 
 * Each model object is in itself stateless.  All state is maintained in the Model Store.  
 * The Document URI uniquely identifies the document containing the model object.
 * 
 * The concrete classes are expected to implements getters for the model class properties which translate
 * into calls to the getTYPEPropertyValue where TYPE is the type of value to be returned and the property descriptor
 * is passed as a parameter.
 * 
 * There are 2 methods of setting values:
 *   - call the setPropertyValue, clearValueCollection or addValueToCollection methods - this will call the modelStore and store the
 *     value immediately
 *   - Gather a list of updates by calling the updatePropertyValue, updateClearValueList, or updateAddPropertyValue
 *     methods.  These methods return a ModelUpdate which can be applied later by calling the <code>apply()</code> method.
 *     A convenience method <code>Write.applyUpdatesInOneTransaction</code> will perform all updates within
 *     a single transaction. This method may result in higher performance updates for some Model Store implementations.
 *     Note that none of the updates will be applied until the storage manager update method is invoked.
 * 
 * Property values are restricted to the following types:
 *   - String - Java Strings
 *   - Booolean - Java Boolean or primitive boolean types
 *   - ModelObjectV2 - A concrete subclass of this type
 *   - {@literal Collection<T>} - A Collection of type T where T is one of the supported non-collection types
 *     
 * This class also handles the conversion of a ModelObjectV2 to and from a TypeValue for storage in the ModelStore.
 *
 */
public abstract class ModelObjectV2 extends CoreModelObject {
	
	static final Logger logger = LoggerFactory.getLogger(ModelObjectV2.class);
	
	public static final String LATEST_SPDX_2_VERSION = "SPDX-2.3";
	
	private String documentUri;
	private String id;

	/**
	 * @throws InvalidSPDXAnalysisException
	 */
	public ModelObjectV2() throws InvalidSPDXAnalysisException {
		super(LATEST_SPDX_2_VERSION);
		updateIdAndDocumentUri();
	}

	/**
	 * Open or create a model object with the default store and default document URI
	 * @param id ID for this object - must be unique within the SPDX document
	 * @throws InvalidSPDXAnalysisException 
	 */
	public ModelObjectV2(String id) throws InvalidSPDXAnalysisException {
		super(CompatibleModelStoreWrapper.documentUriIdToUri(DefaultModelStore.getDefaultDocumentUri(), id, DefaultModelStore.getDefaultModelStore()), LATEST_SPDX_2_VERSION);
		this.documentUri = DefaultModelStore.getDefaultDocumentUri();
		this.id = id;
	}

	/**
	 * @param modelStore Storage for the model objects
	 * @param documentUri SPDX Document URI for a document associated with this model
	 * @param identifier ID for this object - must be unique within the SPDX document
	 * @param copyManager - if supplied, model objects will be implicitly copied into this model store and document URI when referenced by setting methods
	 * @param create - if true, the object will be created in the store if it is not already present
	 * @throws InvalidSPDXAnalysisException
	 */
	public ModelObjectV2(IModelStore modelStore, String documentUri, String identifier, @Nullable IModelCopyManager copyManager, 
			boolean create) throws InvalidSPDXAnalysisException {
		super(modelStore, CompatibleModelStoreWrapper.documentUriIdToUri(documentUri, identifier, modelStore), copyManager, create, LATEST_SPDX_2_VERSION);
		Objects.requireNonNull(modelStore, "Model Store can not be null");
		Objects.requireNonNull(documentUri, "Document URI can not be null");
		Objects.requireNonNull(identifier, "ID can not be null");
		if (identifier.startsWith(documentUri)) {
			logger.warn("document URI was passed in as an ID: "+identifier);
			this.id = identifier.substring(documentUri.length());
			if (this.id.startsWith("#")) {
				this.id = this.id.substring(1);
			}
		} else {
			this.id = identifier;
		}
		this.documentUri = documentUri;
//		if ((LicenseInfoFactory.isSpdxListedLicenseId(id) || LicenseInfoFactory.isSpdxListedExceptionId(id)) &&
//				!SpdxConstantsCompatV2.LISTED_LICENSE_URL.equals(documentUri)) {
//			logger.warn("Listed license document URI changed to listed license URL for documentUri "+documentUri);
//			this.documentUri = SpdxConstantsCompatV2.LISTED_LICENSE_URL;
//		} else {
//			this.documentUri = documentUri;
//		}
		//TODO: See if there is a way to do this check as an independant library
	}

	/**
	 * @param builder
	 * @throws InvalidSPDXAnalysisException
	 */
	public ModelObjectV2(CoreModelObjectBuilder builder)
			throws InvalidSPDXAnalysisException {
		super(builder, LATEST_SPDX_2_VERSION);
		updateIdAndDocumentUri();
	}

	
	/**
	 * Updates the ID and document URI based on the object URI and/or what is available in the store
	 * @throws DefaultStoreNotInitialized 
	 */
	private void updateIdAndDocumentUri() throws DefaultStoreNotInitialized {
		if (objectUri.contains("#")) {
			int index = objectUri.lastIndexOf('#');
			documentUri = objectUri.substring(0, index);
			id = objectUri.substring(index + 1);
			// TODO: Test to make sure I'm not off by one
		} else {
			documentUri = DefaultModelStore.getDefaultDocumentUri();
			id = objectUri;
		}
	}
	
	@Override
	public List<String> _verify(Set<String> verifiedElementIds, String specVersion, List<IndividualUriValue> profiles) {
		// No profiles are used in SPDX 2.X
		return _verify(verifiedElementIds, specVersion);
	}
	
	/**
	 * Implementation of the specific verifications for this model object
	 * @param specVersion Version of the SPDX spec to verify against
	 * @param verifiedElementIds list of all Element Id's which have already been verified - prevents infinite recursion
	 * @return Any verification errors or warnings associated with this object
	 */
	protected abstract List<String> _verify(Set<String> verifiedElementIds, String specVersion);
	
	/**
	 * @param specVersion Version of the SPDX spec to verify against
	 * @param verifiedIElementds list of all element Id's which have already been verified - prevents infinite recursion
	 * @return Any verification errors or warnings associated with this object
	 */
	public List<String> verify(Set<String> verifiedIElementds, String specVersion) {
		if (verifiedIElementds.contains(this.id)) {
			return new ArrayList<>();
		} else {
			// The verifiedElementId is added in the SpdxElement._verify method
			return _verify(verifiedIElementds, specVersion);
		}
	}
	
	/**
	 * @return the Document URI for this object
	 */
	public String getDocumentUri() {
		return this.documentUri;
	}
	
	/**
	 * @return ID for the object
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param propertyDescriptor property descriptor for the object in question
	 * @return true if the object is "to" part of a relationship
	 */
	public boolean isRelatedElement(PropertyDescriptor propertyDescriptor) {
		return SpdxConstantsCompatV2.PROP_RELATED_SPDX_ELEMENT.equals(propertyDescriptor);
	}
	
	//The following methods are to manage the properties associated with the model object
	
	/**
	 * Converts property values to an AnyLicenseInfo if possible - if NONE or NOASSERTION URI value, convert to the appropriate license
	 * @param propertyDescriptor descriptor for the property
	 * @return AnyLicenseInfo license info for the property
	 * @throws InvalidSPDXAnalysisException
	 */
	@SuppressWarnings("unchecked")
	protected Optional<AnyLicenseInfo> getAnyLicenseInfoPropertyValue(PropertyDescriptor propertyDescriptor) throws InvalidSPDXAnalysisException {
		Optional<Object> result = getObjectPropertyValue(propertyDescriptor);
		if (!result.isPresent()) {
			return Optional.empty();
		} else if (result.get() instanceof AnyLicenseInfo) {
			return (Optional<AnyLicenseInfo>)(Optional<?>)result;
		} else if (result.get() instanceof IndividualUriValue) {
			String uri = ((IndividualUriValue)result.get()).getIndividualURI();
			if (SpdxConstantsCompatV2.URI_VALUE_NONE.equals(uri)) {
				return Optional.of(new SpdxNoneLicense());
			} else if (SpdxConstantsCompatV2.URI_VALUE_NOASSERTION.equals(uri)) {
				return Optional.of(new SpdxNoAssertionLicense());
			} else {
				logger.error("Can not convert a URI value to a license: "+uri);
				throw new SpdxInvalidTypeException("Can not convert a URI value to a license: "+uri);
			}
		} else {
			logger.error("Invalid type for AnyLicenseInfo property: "+result.get().getClass().toString());
			throw new SpdxInvalidTypeException("Invalid type for AnyLicenseInfo property: "+result.get().getClass().toString());
		}
	}
	
	/**
	 * Converts property values to an SpdxElement if possible - if NONE or NOASSERTION URI value, convert to the appropriate SpdxElement
	 * @param propertyDescriptor Descriptor for the property
	 * @return SpdxElement stored
	 * @throws InvalidSPDXAnalysisException
	 */
	@SuppressWarnings("unchecked")
	protected Optional<SpdxElement> getElementPropertyValue(PropertyDescriptor propertyDescriptor) throws InvalidSPDXAnalysisException {
		Optional<Object> result = getObjectPropertyValue(propertyDescriptor);
		if (!result.isPresent()) {
			return Optional.empty();
		} else if (result.get() instanceof SpdxElement) {
			return (Optional<SpdxElement>)(Optional<?>)result;
		} else if (result.get() instanceof IndividualUriValue) {
			String uri = ((IndividualUriValue)result.get()).getIndividualURI();
			if (SpdxConstantsCompatV2.URI_VALUE_NONE.equals(uri)) {
				return Optional.of(new SpdxNoneElement());
			} else if (SpdxConstantsCompatV2.URI_VALUE_NOASSERTION.equals(uri)) {
				return Optional.of(new SpdxNoAssertionElement());
			} else {
				logger.error("Can not convert a URI value to an SPDX element: "+uri);
				throw new SpdxInvalidTypeException("Can not convert a URI value to an SPDX element: "+uri);
			}
		} else {
			logger.error("Invalid type for SpdxElement property: "+result.get().getClass().toString());
			throw new SpdxInvalidTypeException("Invalid type for SpdxElement property: "+result.get().getClass().toString());
		}
	}

	@Override
	public int hashCode() {
		if (this.id != null) {
			return this.id.toLowerCase().hashCode() ^ this.documentUri.hashCode();
		} else {
			return 0;
		}
	}
	/* (non-Javadoc)
	 * @see org.spdx.rdfparser.license.AnyLicenseInfo#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ModelObjectV2)) {
			// covers o == null, as null is not an instance of anything
			return false;
		}
		ModelObjectV2 comp = (ModelObjectV2)o;
		if (getModelStore().getIdType(id).equals(IdType.Anonymous)) {
			return Objects.equals(modelStore, comp.getModelStore()) && Objects.equals(id, comp.getId()) && Objects.equals(documentUri, comp.getDocumentUri());
		} else {
			return Objects.equals(id, comp.getId()) && Objects.equals(documentUri, comp.getDocumentUri());
		}
	}
	
	@Override
	public TypedValue toTypedValue() throws InvalidSPDXAnalysisException {
		return CompatibleModelStoreWrapper.typedValueFromDocUri(this.documentUri, this.id, modelStore.getIdType(id).equals(IdType.Anonymous), this.getType());
	}
	
	// The following methods are helper methods to create Model Object subclasses using the same model store and document as this Model Object

	/**
	 * @param annotator This field identifies the person, organization or tool that has commented on a file, package, or the entire document.
	 * @param annotationType This field describes the type of annotation.  Annotations are usually created when someone reviews the file, and if this is the case the annotation type should be REVIEW.   If the author wants to store extra information about one of the elements during creation, it is recommended to use the type of OTHER.
	 * @param date Identify when the comment was made.  This is to be specified according to the combined date and time in the UTC format, as specified in the ISO 8601 standard.
	 * @param comment
	 * @return
	 * @throws InvalidSPDXAnalysisException
	 */
	public Annotation createAnnotation(String annotator, AnnotationType annotationType, String date,
			String comment) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(annotator, "Annotator can not be null");
		Objects.requireNonNull(annotationType, "AnnotationType can not be null");
		Objects.requireNonNull(date, "Date can not be null");
		Objects.requireNonNull(comment, "Comment can not be null");
		Annotation retval = new Annotation(this.modelStore, this.documentUri, 
				this.modelStore.getNextId(IdType.Anonymous), copyManager, true);
		retval.setAnnotationDate(date);
		retval.setAnnotationType(annotationType);
		retval.setAnnotator(annotator);
		retval.setComment(comment);
		return retval;
	}
	
	/**
	 * @param relatedElement   The SPDX Element that is related
	 * @param relationshipType Type of relationship - See the specification for a
	 *                         description of the types
	 * @param comment          optional comment for the relationship
	 * @return
	 * @throws InvalidSPDXAnalysisException
	 */
	public Relationship createRelationship(SpdxElement relatedElement, 
			RelationshipType relationshipType, @Nullable String comment) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(relatedElement, "Related Element can not be null");
		Objects.requireNonNull(relationshipType, "Relationship type can not be null");
		Relationship retval = new Relationship(this.modelStore, this.documentUri, 
				this.modelStore.getNextId(IdType.Anonymous), this.copyManager, true);
		retval.setRelatedSpdxElement(relatedElement);
		retval.setRelationshipType(relationshipType);
		if (Objects.nonNull(comment)) {
			retval.setComment(comment);
		}
		return retval;
	}
	
	/**
	 * @param algorithm Checksum algorithm
	 * @param value Checksum value
	 * @return Checksum using the same model store and document URI as this Model Object
	 * @throws InvalidSPDXAnalysisException
	 */
	public Checksum createChecksum(ChecksumAlgorithm algorithm, String value) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(algorithm, "Algorithm can not be null");
		Objects.requireNonNull(value, "Value can not be null");
		Checksum retval = new Checksum(this.modelStore, this.documentUri, 
				this.modelStore.getNextId(IdType.Anonymous), this.copyManager, true);
		retval.setAlgorithm(algorithm);
		retval.setValue(value);
		return retval;
	}
	
	/**
	 * @param value Verification code calculated value
	 * @param excludedFileNames file names of files excluded from the verification code calculation
	 * @return Package verification code using the same model store and document URI as this Model Object
	 * @throws InvalidSPDXAnalysisException
	 */
	public SpdxPackageVerificationCode createPackageVerificationCode(String value, Collection<String> excludedFileNames) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(value, "Value can not be null");
		Objects.requireNonNull(excludedFileNames, "Excluded Files can not be null");
		SpdxPackageVerificationCode retval = new SpdxPackageVerificationCode(this.modelStore, this.documentUri, 
				this.modelStore.getNextId(IdType.Anonymous), this.copyManager, true);
		retval.setValue(value);
		retval.getExcludedFileNames().addAll(excludedFileNames);
		return retval;
	}
	
	/**
	 * @param externalDocumentUri Document URI for the external document
	 * @param checksum Checksum of the external Document
	 * @param externalDocumentId ID to be used internally within this SPDX document
	 * @return ExternalDocumentRef using the same model store and document URI as this Model Object
	 * @throws InvalidSPDXAnalysisException 
	 */
	public ExternalDocumentRef createExternalDocumentRef(String externalDocumentId, String externalDocumentUri, 
			Checksum checksum) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(externalDocumentUri, "External document URI can not be null");
		Objects.requireNonNull(checksum, "Checksum can not be null");
		Objects.requireNonNull(externalDocumentId, "External document ID can not be null");
		if (!SpdxVerificationHelper.isValidExternalDocRef(externalDocumentId)) {
			throw new InvalidSPDXAnalysisException("Invalid external document reference ID "+externalDocumentId+
					".  Must be of the format "+SpdxConstantsCompatV2.EXTERNAL_DOC_REF_PATTERN.pattern());
		}
		if (!SpdxVerificationHelper.isValidUri(externalDocumentUri)) {
			throw new InvalidSPDXAnalysisException("Invalid external document URI: "+externalDocumentUri);
		}
		IModelStoreLock lock = modelStore.enterCriticalSection(false);
		try {
			if (modelStore.exists(CompatibleModelStoreWrapper.documentUriIdToUri(getDocumentUri(), externalDocumentId, modelStore))) {
				return new ExternalDocumentRef(getModelStore(), getDocumentUri(), 
						externalDocumentId, this.copyManager, false);
			} else {
				ExternalDocumentRef retval = new ExternalDocumentRef(getModelStore(), getDocumentUri(), 
						externalDocumentId, this.copyManager, true);
				retval.setChecksum(checksum);
				retval.setSpdxDocumentNamespace(externalDocumentUri);
				// Need to add this to the list of document URI's
				ModelObjectHelper.addValueToCollection(getModelStore(), 
						CompatibleModelStoreWrapper.documentUriIdToUri(getDocumentUri(), SpdxConstantsCompatV2.SPDX_DOCUMENT_ID, false),
						SpdxConstantsCompatV2.PROP_SPDX_EXTERNAL_DOC_REF,
						retval, copyManager);
				return retval;
			}
		} finally {
			getModelStore().leaveCriticalSection(lock);
		}
	}

	/**
	 * @param creators Creators Identify who (or what, in the case of a tool) created the SPDX file.  If the SPDX file was created by an individual, indicate the person's name. 
	 * @param date When the SPDX file was originally created. The date is to be specified according to combined date and time in UTC format as specified in ISO 8601 standard. 
	 * @return creationInfo using the same modelStore and documentUri as this object
	 * @throws InvalidSPDXAnalysisException 
	 */
	public SpdxCreatorInformation createCreationInfo(List<String> creators, String date) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(creators, "Creators can not be null");
		Objects.requireNonNull(date, "Date can not be null");
		SpdxCreatorInformation retval = new SpdxCreatorInformation(modelStore, documentUri, 
				modelStore.getNextId(IdType.Anonymous), copyManager, true);
		retval.getCreators().addAll(creators);
		retval.setCreated(date);
		return retval;
	}
	
	/**
	 * @param category Reference category
	 * @param referenceType Reference type
	 * @param locator Reference locator
	 * @param comment Optional comment
	 * @return ExternalRef using the same modelStore and documentUri as this object
	 * @throws InvalidSPDXAnalysisException
	 */
	public ExternalRef createExternalRef(ReferenceCategory category, ReferenceType referenceType, 
			String locator, @Nullable String comment) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(category, "Category can not be null");
		Objects.requireNonNull(referenceType, "Reference type can not be null");
		Objects.requireNonNull(locator, "Locator can not be null");
		ExternalRef retval = new ExternalRef(modelStore, documentUri, 
				modelStore.getNextId(IdType.Anonymous), copyManager, true);
		retval.setReferenceCategory(category);
		retval.setReferenceType(referenceType);
		retval.setReferenceLocator(locator);
		retval.setComment(comment);
		return retval;
	}
	
	/**
	 * Create an SpdxFileBuilder with all of the required properties - the build() method will build the file
	 * @param id - ID - must be an SPDX ID type
	 * @param name - File name
	 * @param concludedLicense license concluded
	 * @param seenLicense collection of seen licenses
	 * @param copyrightText Copyright text
	 * @param sha1 Sha1 checksum
	 * @return SPDX file using the same modelStore and documentUri as this object
	 * @throws InvalidSPDXAnalysisException
	 */
	public SpdxFile.SpdxFileBuilder createSpdxFile(String id, String name, AnyLicenseInfo concludedLicense,
			Collection<AnyLicenseInfo> seenLicense, String copyrightText, Checksum sha1) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(id, "ID can not be null");
		Objects.requireNonNull(name, "Name can not be null");
		Objects.requireNonNull(sha1, "Sha1 can not be null");
		return new SpdxFile.SpdxFileBuilder(modelStore, documentUri, id, copyManager,
				name, concludedLicense, seenLicense, copyrightText, sha1);
	}
	
	/**
	 * Create an SpdxPackageBuilder with all required fields for a filesAnalyzed=false using this objects model store and document URI
	 * @param id - ID - must be an SPDX ID type
	 * @param name - File name
	 * @param concludedLicense license concluded
	 * @param copyrightText Copyright text
	 * @param licenseDeclared Declared license for the package
	 * @return SpdxPackageBuilder with all required fields for a filesAnalyzed=false
	 */
	public SpdxPackage.SpdxPackageBuilder createPackage(String id, String name,
				AnyLicenseInfo concludedLicense, 
				String copyrightText, AnyLicenseInfo licenseDeclared) {
		Objects.requireNonNull(id, "ID can not be null");
		Objects.requireNonNull(name, "Name can not be null");
		return new SpdxPackage.SpdxPackageBuilder(modelStore, documentUri, id, copyManager,
				name, concludedLicense, copyrightText, licenseDeclared);
	}

	/**
	 * @param referencedElement
	 * @param offset
	 * @return ByteOffsetPointer using the same modelStore and documentUri as this object
	 * @throws InvalidSPDXAnalysisException 
	 */
	public ByteOffsetPointer createByteOffsetPointer(SpdxElement referencedElement, int offset) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(referencedElement, "Referenced element can not be null");
		ByteOffsetPointer retval = new ByteOffsetPointer(modelStore, documentUri, 
				modelStore.getNextId(IdType.Anonymous), copyManager, true);
		retval.setReference(referencedElement);
		retval.setOffset(offset);
		return retval;
	}

	/**
	 * @param referencedElement
	 * @param lineNumber
	 * @return LineCharPointer using the same modelStore and documentUri as this object
	 * @throws InvalidSPDXAnalysisException 
	 */
	public LineCharPointer createLineCharPointer(SpdxElement referencedElement, int lineNumber) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(referencedElement, "Referenced element can not be null");
		LineCharPointer retval = new LineCharPointer(modelStore, documentUri, 
				modelStore.getNextId(IdType.Anonymous), copyManager, true);
		retval.setReference(referencedElement);
		retval.setLineNumber(lineNumber);
		return retval;
	}
	
	/**
	 * @param startPointer
	 * @param endPointer
	 * @return StartEndPointer using the same modelStore and documentUri as this object
	 * @throws InvalidSPDXAnalysisException
	 */
	public StartEndPointer createStartEndPointer(SinglePointer startPointer, SinglePointer endPointer) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(startPointer, "Start pointer can not be null");
		Objects.requireNonNull(endPointer, "End pointer can not be null");
		StartEndPointer retval = new StartEndPointer(modelStore, documentUri, 
				modelStore.getNextId(IdType.Anonymous), copyManager, true);
		retval.setStartPointer(startPointer);
		retval.setEndPointer(endPointer);
		return retval;
	}
	
	/**
	 * Create an SpdxSnippetBuilder with all of the required properties - the build() method will build the file
	 * @param id - ID - must be an SPDX ID type
	 * @param name - File name
	 * @param concludedLicense license concluded
	 * @param seenLicense collection of seen licenses
	 * @param copyrightText Copyright text
	 * @param snippetFromFile File where the snippet is located
	 * @param startByte first byte of the snippet in the file
	 * @param endByte last byte of the snippet in the file
	 * @return SPDX snippet using the same modelStore and documentUri as this object
	 * @throws InvalidSPDXAnalysisException
	 */
	public SpdxSnippet.SpdxSnippetBuilder createSpdxSnippet(String id, String name, AnyLicenseInfo concludedLicense,
			Collection<AnyLicenseInfo> seenLicense, String copyrightText, 
			SpdxFile snippetFromFile, int startByte, int endByte) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(id, "ID can not be null");
		Objects.requireNonNull(name, "Name can not be null");
		return new SpdxSnippet.SpdxSnippetBuilder(modelStore, documentUri, id, copyManager,
				name, concludedLicense, seenLicense, copyrightText, snippetFromFile, startByte, endByte);
	}
	
	/**
	 * @param members
	 * @return  ConjunctiveLicenseSet with default model store and document URI initialized with members
	 * @throws InvalidSPDXAnalysisException 
	 */
	public ConjunctiveLicenseSet createConjunctiveLicenseSet(Collection<AnyLicenseInfo> members) throws InvalidSPDXAnalysisException {
		ConjunctiveLicenseSet retval = new ConjunctiveLicenseSet(modelStore, documentUri, 
				modelStore.getNextId(IdType.Anonymous), copyManager, true);
		retval.setMembers(members);
		return retval;
	}
	
	/**
	 * @param members
	 * @return  DisjunctiveLicenseSet with default model store and document URI initialized with members
	 * @throws InvalidSPDXAnalysisException 
	 */
	public DisjunctiveLicenseSet createDisjunctiveLicenseSet(Collection<AnyLicenseInfo> members) throws InvalidSPDXAnalysisException {
		DisjunctiveLicenseSet retval = new DisjunctiveLicenseSet(modelStore, documentUri, 
				modelStore.getNextId(IdType.Anonymous), copyManager, true);
		retval.setMembers(members);
		return retval;
	}
	
	/**
	 * Create a CrossRef Builder with an Anonymous ID type using the same model store and document URI
	 * @param url URL for the cross reference
	 * @return a CrossRefBuilder which you can call <code>build()</code> on to build the CrossRef
	 * @throws InvalidSPDXAnalysisException
	 */
	public CrossRefBuilder createCrossRef(String url) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(url, "URL can not be null");
		return new CrossRefBuilder(this.modelStore, this.documentUri, 
				this.modelStore.getNextId(IdType.Anonymous), this.copyManager, url);
	}
	
	@Override
	public String toString() {
		return this.getType() + " " + this.id;
	}

}
