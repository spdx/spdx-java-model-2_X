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

import org.spdx.core.IModelCopyManager;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.SpdxIdNotFoundException;
import org.spdx.core.TypedValue;
import org.spdx.library.model.v2.license.AnyLicenseInfo;
import org.spdx.library.model.v2.license.SpdxListedLicense;
import org.spdx.storage.CompatibleModelStoreWrapper;
import org.spdx.storage.IModelStore;
import org.spdx.storage.IModelStore.IdType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class to create ModelObjects based on the type and SPDX Spec Version
 * Types are defined classes in the SpdxConstantsCompatV2 class and map to the standard SPDX model
 * 
 * @author Gary O'Neall
 *
 */
public class SpdxModelFactory {
	
	static final Logger logger = LoggerFactory.getLogger(SpdxModelFactory.class);
	
	public static Map<String, Class<?>> SPDX_TYPE_TO_CLASS_V2;
	public static Map<Class<?>, String> SPDX_CLASS_TO_TYPE;
	static {
		Map<String, Class<?>> typeToClassV2 = new HashMap<>();
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_DOCUMENT, org.spdx.library.model.v2.SpdxDocument.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_PACKAGE, org.spdx.library.model.v2.SpdxPackage.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_CREATION_INFO, org.spdx.library.model.v2.SpdxCreatorInformation.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_CHECKSUM, org.spdx.library.model.v2.Checksum.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_ANY_LICENSE_INFO, org.spdx.library.model.v2.license.AnyLicenseInfo.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_SIMPLE_LICENSE_INFO, org.spdx.library.model.v2.license.SimpleLicensingInfo.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_CONJUNCTIVE_LICENSE_SET, org.spdx.library.model.v2.license.ConjunctiveLicenseSet.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_DISJUNCTIVE_LICENSE_SET, org.spdx.library.model.v2.license.DisjunctiveLicenseSet.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_EXTRACTED_LICENSING_INFO, org.spdx.library.model.v2.license.ExtractedLicenseInfo.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_LICENSE, org.spdx.library.model.v2.license.License.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_LISTED_LICENSE, org.spdx.library.model.v2.license.SpdxListedLicense.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_LICENSE_EXCEPTION, org.spdx.library.model.v2.license.LicenseException.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_LISTED_LICENSE_EXCEPTION, org.spdx.library.model.v2.license.ListedLicenseException.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_OR_LATER_OPERATOR, org.spdx.library.model.v2.license.OrLaterOperator.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_WITH_EXCEPTION_OPERATOR, org.spdx.library.model.v2.license.WithExceptionOperator.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_FILE, org.spdx.library.model.v2.SpdxFile.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_VERIFICATIONCODE, org.spdx.library.model.v2.SpdxPackageVerificationCode.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_ANNOTATION, org.spdx.library.model.v2.Annotation.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_RELATIONSHIP, org.spdx.library.model.v2.Relationship.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_ITEM, org.spdx.library.model.v2.SpdxItem.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_ELEMENT, org.spdx.library.model.v2.SpdxElement.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_NONE_ELEMENT, org.spdx.library.model.v2.SpdxNoneElement.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_NOASSERTION_ELEMENT, org.spdx.library.model.v2.SpdxNoAssertionElement.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_EXTERNAL_DOC_REF, org.spdx.library.model.v2.ExternalDocumentRef.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_EXTERNAL_REFERENCE, org.spdx.library.model.v2.ExternalRef.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_REFERENCE_TYPE, org.spdx.library.model.v2.ReferenceType.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SPDX_SNIPPET, org.spdx.library.model.v2.SpdxSnippet.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_NOASSERTION_LICENSE, org.spdx.library.model.v2.license.SpdxNoAssertionLicense.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_NONE_LICENSE, org.spdx.library.model.v2.license.SpdxNoneLicense.class);
		typeToClassV2.put(org.spdx.library.model.v2.GenericModelObject.GENERIC_MODEL_OBJECT_TYPE, org.spdx.library.model.v2.GenericModelObject.class);
		typeToClassV2.put(org.spdx.library.model.v2.GenericSpdxElement.GENERIC_SPDX_ELEMENT_TYPE, org.spdx.library.model.v2.GenericSpdxElement.class);
		typeToClassV2.put(org.spdx.library.model.v2.GenericSpdxItem.GENERIC_SPDX_ITEM_TYPE, org.spdx.library.model.v2.GenericSpdxItem.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_EXTERNAL_SPDX_ELEMENT, org.spdx.library.model.v2.ExternalSpdxElement.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_POINTER_START_END_POINTER, org.spdx.library.model.v2.pointer.StartEndPointer.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_POINTER_BYTE_OFFSET_POINTER, org.spdx.library.model.v2.pointer.ByteOffsetPointer.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_POINTER_LINE_CHAR_POINTER, org.spdx.library.model.v2.pointer.LineCharPointer.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_POINTER_COMPOUNT_POINTER, org.spdx.library.model.v2.pointer.CompoundPointer.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_SINGLE_POINTER, org.spdx.library.model.v2.pointer.SinglePointer.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_CROSS_REF, org.spdx.library.model.v2.license.CrossRef.class);
		typeToClassV2.put(SpdxConstantsCompatV2.ENUM_FILE_TYPE, org.spdx.library.model.v2.enumerations.FileType.class);
		typeToClassV2.put(SpdxConstantsCompatV2.ENUM_ANNOTATION_TYPE, org.spdx.library.model.v2.enumerations.AnnotationType.class);
		typeToClassV2.put(SpdxConstantsCompatV2.ENUM_CHECKSUM_ALGORITHM_TYPE, org.spdx.library.model.v2.enumerations.ChecksumAlgorithm.class);
		typeToClassV2.put(SpdxConstantsCompatV2.ENUM_REFERENCE_CATEGORY_TYPE, org.spdx.library.model.v2.enumerations.ReferenceCategory.class);
		typeToClassV2.put(SpdxConstantsCompatV2.ENUM_REFERENCE_RELATIONSHIP_TYPE, org.spdx.library.model.v2.enumerations.RelationshipType.class);
		typeToClassV2.put(SpdxConstantsCompatV2.CLASS_EXTERNAL_EXTRACTED_LICENSE, org.spdx.library.model.v2.license.ExternalExtractedLicenseInfo.class);	
		typeToClassV2.put(SpdxConstantsCompatV2.ENUM_PURPOSE, org.spdx.library.model.v2.enumerations.Purpose.class);
		SPDX_TYPE_TO_CLASS_V2 = Collections.unmodifiableMap(typeToClassV2);
		Map<Class<?>, String> classToType = new HashMap<>();
		for (Entry<String, Class<?>> entry:typeToClassV2.entrySet()) {
			classToType.put(entry.getValue(), entry.getKey());
		}
		SPDX_CLASS_TO_TYPE = Collections.unmodifiableMap(classToType);
	}

	
	
	/**
	 * Create an SPDX version 2.X document with default values for creator, created, licenseListVersion, data license and specVersion
	 * @param modelStore Where to store the SPDX Document
	 * @param documentUri unique URI for the SPDX document
	 * @param copyManager if non-null, allows for copying of properties from other model stores or document URI's when referenced
	 * @return
	 * @throws InvalidSPDXAnalysisException
	 */
	public static org.spdx.library.model.v2.SpdxDocument createSpdxDocumentV2(IModelStore modelStore, String documentUri, IModelCopyManager copyManager) throws InvalidSPDXAnalysisException {
		org.spdx.library.model.v2.SpdxDocument retval = new org.spdx.library.model.v2.SpdxDocument(modelStore, documentUri, copyManager, true);
		String date = new SimpleDateFormat(SpdxConstantsCompatV2.SPDX_DATE_FORMAT).format(new Date());
		org.spdx.library.model.v2.SpdxCreatorInformation creationInfo = new org.spdx.library.model.v2.SpdxCreatorInformation(
				modelStore, documentUri, modelStore.getNextId(IdType.Anonymous), copyManager, true);
		creationInfo.getCreators().add("Tool: SPDX Tools");
		creationInfo.setCreated(date);
		retval.setCreationInfo(creationInfo);
		retval.setDataLicense(createSpdxDocumentDataLicense(modelStore, documentUri, copyManager));
		retval.setSpecVersion(Version.CURRENT_SPDX_VERSION);
		return retval;
	}
	
	/**
	 * @param modelStore
	 * @param documentUri
	 * @param copyManager
	 * @return
	 * @throws InvalidSPDXAnalysisException 
	 */
	private static AnyLicenseInfo createSpdxDocumentDataLicense(
			IModelStore modelStore, String documentUri,
			IModelCopyManager copyManager) throws InvalidSPDXAnalysisException {
		SpdxListedLicense retval = new SpdxListedLicense(modelStore, documentUri, SpdxConstantsCompatV2.SPDX_DATA_LICENSE_ID, copyManager, true);
		retval.setFsfLibre(true);
		retval.setLicenseText(SpdxConstantsCompatV2.CC0_LICENSE_TEXT);
		retval.setName("Creative Commons Zero v1.0 Universal");
		retval.getSeeAlso().add("https://creativecommons.org/publicdomain/zero/1.0/legalcode");
		return retval;
	}

	/**
	 * Create an SPDX version 2 model object in a model store given the document URI, ID and type
	 * @param modelStore model store where the object is to be created
	 * @param documentUri document URI for the stored item
	 * @param id for the item
	 * @param type SPDX class or type
	 *  @param copyManager if non-null, allows for copying of properties from other model stores or document URI's when referenced
	 * @return a ModelObjectV2 of type type
	 * @throws InvalidSPDXAnalysisException
	 */
	public static org.spdx.library.model.v2.ModelObjectV2 createModelObjectV2(IModelStore modelStore, String documentUri, String id,
			String type, IModelCopyManager copyManager) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(modelStore, "Model store can not be null");
		Objects.requireNonNull(documentUri, "A document URI or namespace must be supplied for all SPDX version 2 model objects");
		Objects.requireNonNull(id, "ID must not be null");
		return getModelObjectV2(modelStore, documentUri, id, type, copyManager, true);
	 }
	
	/**
	 * Create an SPDX spec version 2.X model object in a model store given the document URI, ID and type
	 * @param modelStore model store where the object is to be created
	 * @param documentUri document URI for the stored item
	 * @param id ID for the item
	 * @param type SPDX class or type
	 * @param copyManager if non-null, allows for copying of properties from other model stores or document URI's when referenced
	 * @param create if true, create the model object if it does not already exist
	 * @return a ModelObjectV2 of type type
	 * @throws InvalidSPDXAnalysisException
	 */
	public static org.spdx.library.model.v2.ModelObjectV2 getModelObjectV2(IModelStore modelStore, String documentUri, String id,
			String type, IModelCopyManager copyManager, boolean create) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(modelStore, "Model store can not be null");
		Objects.requireNonNull(documentUri, "A document URI or namespace must be supplied for all SPDX version 2 model objects");
		if (SpdxConstantsCompatV2.CLASS_SPDX_DOCUMENT.equals(type)) {
			// Special case since document does not have the same constructor method signature - the ID is ignored
			return new org.spdx.library.model.v2.SpdxDocument(modelStore, documentUri, copyManager, create);
		}
		if (SpdxConstantsCompatV2.CLASS_SPDX_REFERENCE_TYPE.equals(type)) {
			throw new InvalidSPDXAnalysisException("Reference type can only be created with a type supplied.");
		}
		if (SpdxConstantsCompatV2.CLASS_SPDX_REVIEW.equals(type)) {
			throw new InvalidSPDXAnalysisException("Review class is no longer supported");
		}
		Objects.requireNonNull(id, "ID must not be null");
		Class<?> clazz = SPDX_TYPE_TO_CLASS_V2.get(type);
		if (Objects.isNull(clazz)) {
			throw new InvalidSPDXAnalysisException("Unknown SPDX version 2 type: "+type);
		}
		if (Modifier.isAbstract(clazz.getModifiers())) {
			throw new InvalidSPDXAnalysisException("Can not instantiate an abstract class for the SPDX version 2 type: "+type);
		}
		try {
			Constructor<?> con = clazz.getDeclaredConstructor(IModelStore.class, String.class, String.class, IModelCopyManager.class, boolean.class);
			return (org.spdx.library.model.v2.ModelObjectV2)con.newInstance(modelStore, documentUri, id, copyManager, create);
		} catch (NoSuchMethodException e) {
			throw new InvalidSPDXAnalysisException("Could not create the model object SPDX version 2 type: "+type);
		} catch (SecurityException e) {
			throw new InvalidSPDXAnalysisException("Unexpected security exception for SPDX version 2 type: "+type, e);
		} catch (InstantiationException e) {
			throw new InvalidSPDXAnalysisException("Unexpected instantiation exception for SPDX version 2 type: "+type, e);
		} catch (IllegalAccessException e) {
			throw new InvalidSPDXAnalysisException("Unexpected illegal access exception for SPDX version 2 type: "+type, e);
		} catch (IllegalArgumentException e) {
			throw new InvalidSPDXAnalysisException("Unexpected illegal argument exception for SPDX version 2 type: "+type, e);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof InvalidSPDXAnalysisException) {
				throw (InvalidSPDXAnalysisException)e.getTargetException();
			} else {
				throw new InvalidSPDXAnalysisException("Unexpected invocation target exception for SPDX version 2 type: "+type, e);
			}
		}
	}

	/**
	 * @param modelStore Store for the SPDX Spec version 2 model
	 * @param documentUri Document URI for for the ID
	 * @param copyManager Optional copy manager for copying any properties from other model
	 * @param objectUri ID for the model object
	 * @return SPDX Version 2 compatible ModelObjectV2 with the ID in the model store
	 * @throws InvalidSPDXAnalysisException 
	 */
	public static Optional<org.spdx.library.model.v2.ModelObjectV2> getModelObjectV2(IModelStore modelStore, String documentUri,
			String id, @Nullable IModelCopyManager copyManager) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(modelStore, "Model store can not be null");
		Objects.requireNonNull(documentUri, "A document URI or namespace must be supplied for all SPDX version 2 model objects");
		Objects.requireNonNull(id, "ID must not be null");
		if (id.contains(":")) {
			// External document ref
			try {
				return Optional.of(new org.spdx.library.model.v2.ExternalSpdxElement(modelStore, documentUri, id, copyManager, true));
			} catch(InvalidSPDXAnalysisException ex) {
				logger.warn("Attempting to get a model object for an invalid SPDX ID.  Returning empty");
				return Optional.empty();
			}
		}
		Optional<TypedValue> tv = modelStore.getTypedValue(
				CompatibleModelStoreWrapper.documentUriIdToUri(documentUri, id, modelStore.getIdType(id).equals(IdType.Anonymous)));
		if (tv.isPresent()) {
			String type = tv.get().getType();
			try {
				return Optional.of(getModelObjectV2(modelStore, documentUri, id, type, copyManager, false));
			} catch(SpdxIdNotFoundException ex) {
				return Optional.empty();	// There is a window where the ID disappears between getTypedValue and getModelObject
			}
		} else {
			if (SpdxConstantsCompatV2.NOASSERTION_VALUE.equals(id)) {
				return Optional.of(new org.spdx.library.model.v2.SpdxNoAssertionElement());
			} else if (SpdxConstantsCompatV2.NONE_VALUE.equals(id)) {
				return Optional.of(new org.spdx.library.model.v2.SpdxNoneElement());
			} else {
				return Optional.empty();
			}
		}
	}

	public static ModelObjectV2 createModelObject(IModelStore stModelStore,
			String objectUri, String type, IModelCopyManager copyManager) {
		// TODO Auto-generated method stub
		return null;
	}
}
