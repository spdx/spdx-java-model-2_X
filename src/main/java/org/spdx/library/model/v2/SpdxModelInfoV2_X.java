/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.library.model.v2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

import javax.annotation.Nullable;
import javax.lang.model.element.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spdx.core.CoreModelObject;
import org.spdx.core.DefaultModelStore;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.ISpdxModelInfo;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.library.model.v2.enumerations.SpdxEnumFactoryCompatV2;
import org.spdx.library.model.v2.license.AnyLicenseInfo;
import org.spdx.library.model.v2.license.ExternalExtractedLicenseInfo;
import org.spdx.library.model.v2.license.ExtractedLicenseInfo;
import org.spdx.library.model.v2.license.LicenseException;
import org.spdx.library.model.v2.license.SpdxListedLicense;
import org.spdx.library.model.v2.license.SpdxNoAssertionLicense;
import org.spdx.library.model.v2.license.SpdxNoneLicense;
import org.spdx.storage.IModelStore;
import org.spdx.storage.IModelStore.IdType;

/**
 * Compatible model info for all supported spec version 2 (2.0, 2.1, 2.2, 2.2.1, 2.3)
 *
 * @author Gary O'Neall
 */
public class SpdxModelInfoV2_X implements ISpdxModelInfo {
	
	static final Logger logger = LoggerFactory.getLogger(SpdxModelInfoV2_X.class);
	

	@Override
	public @Nullable CoreModelObject createExternalElement(IModelStore store, String uri,
			IModelCopyManager copyManager, Class<?> type, String specVersion)
			throws InvalidSPDXAnalysisException {
		Matcher licenseMatcher = SpdxConstantsCompatV2.EXTERNAL_EXTRACTED_LICENSE_URI_PATTERN.matcher(uri);
		if (licenseMatcher.matches()) {
			return new ExternalExtractedLicenseInfo(store, licenseMatcher.group(1), licenseMatcher.group(2), copyManager);
		} 
		Matcher elementMatcher = SpdxConstantsCompatV2.EXTERNAL_SPDX_ELEMENT_URI_PATTERN.matcher(uri);
		if (elementMatcher.matches()) {
			return new ExternalSpdxElement(store, elementMatcher.group(1), elementMatcher.group(2), copyManager);
		} else return null;
	}

	@Override
	public List<String> getSpecVersions() {
		return Arrays.asList(new String[] {"SPDX-2.0", "SPDX-2.1", "SPDX-2.2", "SPDX-2.2.1", "SPDX-2.3"});
	}

	@Override
	public Map<String, Enum<?>> getUriToEnumMap() {
		return SpdxEnumFactoryCompatV2.uriToEnum;
	}

	@Override
	public @Nullable Object uriToIndividual(String uri, @Nullable Class<?> type) {
		if (SpdxConstantsCompatV2.URI_VALUE_NONE.equals(uri)) {
			if (Objects.nonNull(type)) {
				if (type.isAssignableFrom(AnyLicenseInfo.class)) {
					try {
						return new SpdxNoneLicense();
					} catch (InvalidSPDXAnalysisException e) {
						logger.warn("Error creating SPDX None License",e);
						return new SpdxNone();
					}
				} else if (type.isAssignableFrom(SpdxElement.class)) {
					try {
						return new SpdxNoneElement();
					} catch (InvalidSPDXAnalysisException e) {
						logger.warn("Error creating SPDX None Element",e);
						return new SpdxNone();
					}
				}
			}
			return new SpdxNone();
			// NOTE: This could represent a NoneLicense or a NoneElement - this should be replaced in context
		} else if (SpdxConstantsCompatV2.URI_VALUE_NOASSERTION.equals(uri)) {
			if (Objects.nonNull(type)) {
				if (type.isAssignableFrom(AnyLicenseInfo.class)) {
					try {
						return new SpdxNoAssertionLicense();
					} catch (InvalidSPDXAnalysisException e) {
						logger.warn("Error creating SPDX NoAssertion License",e);
						return new SpdxNone();
					}
				} else if (type.isAssignableFrom(SpdxElement.class)) {
					try {
						return new SpdxNoAssertionElement();
					} catch (InvalidSPDXAnalysisException e) {
						logger.warn("Error creating SPDX NoAssertion Element",e);
						return new SpdxNone();
					}
				}
			}
			return new SpdxNoAssertion();
			// NOTE: This could represent a NoAssertionLicense or a NoAssertionElement - this should be replaced in context
		} else if (SpdxConstantsCompatV2.REFERENCE_TYPE_URI_PATTERN.matcher(uri).matches()) {
			try {
				return new ReferenceType(uri);
			} catch (InvalidSPDXAnalysisException e) {
				logger.error("Error converting URI to reference type", e);
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public CoreModelObject createModelObject(IModelStore modelStore,
			String objectUri, String type, IModelCopyManager copyManager,
			String specVersion, boolean create, String prefix) throws InvalidSPDXAnalysisException {
		if (!SpdxModelFactoryCompatV2.SPDX_TYPE_TO_CLASS_V2.containsKey(type)) {
			logger.error(type+" not a supported type for SPDX spec version 2.X");
			throw new InvalidSPDXAnalysisException(type+" not a supported type for SPDX spec version 2.X");
		}
		Class<?> typeClass = SpdxModelFactoryCompatV2.SPDX_TYPE_TO_CLASS_V2.get(type);
		if (SpdxListedLicense.class.isAssignableFrom(typeClass)) {
			// check that the URI is a listed license URI
			String id;
			if (objectUri.startsWith(SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX)) {
				id = objectUri.substring(SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX.length());
			} else if (objectUri.startsWith(SpdxConstantsCompatV2.LISTED_LICENSE_URL)) {
				logger.warn("Incorrect namespace for Listed License - 'https:' is used rather than 'http:'");
				id = objectUri.substring(SpdxConstantsCompatV2.LISTED_LICENSE_URL.length());
			} else {
				logger.error("'" + objectUri + "' Listed license URI does not start with " +
								SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX + ".");
				throw new InvalidSPDXAnalysisException("'" + objectUri + "' Listed license URI does not start with " +
						SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX + ".");
			}
			return SpdxModelFactoryCompatV2.getModelObjectV2(modelStore, SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX, 
					id, SpdxConstantsCompatV2.CLASS_SPDX_LISTED_LICENSE, copyManager, true);
		} else if (LicenseException.class.isAssignableFrom(typeClass)) {
			// check that the URI is a listed license URI
			if (!objectUri.startsWith(SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX)) {
				logger.error("'" + objectUri + "' Listed license exception URI does not start with " +
								SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX + ".");
				throw new InvalidSPDXAnalysisException("'" + objectUri + "' Listed exception license URI does not start with " +
						SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX + ".");
			}
			String id = objectUri.substring(SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX.length());
			return SpdxModelFactoryCompatV2.getModelObjectV2(modelStore, SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX, 
					id, SpdxConstantsCompatV2.CLASS_SPDX_LISTED_LICENSE_EXCEPTION, copyManager, true);
		} else if (SpdxElement.class.isAssignableFrom(typeClass)) {
			//TODO: Do we want to check for external references if (modelStore.getExternalReferenceMap(objectUri) != null && ??)
			if (modelStore.isAnon(objectUri)) {
				logger.error("SPDX elements must not be anonomous types - missing ID for "+objectUri);
				throw new InvalidSPDXAnalysisException("SPDX elements must not be anonomous types - missing ID for "+objectUri);
			}
			// Note that the EXTERNAL_SPDX_ELEMENT_URI_PATTERN also matches the full URI for the object
			Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_SPDX_ELEMENT_URI_PATTERN.matcher(objectUri);
			if (!matcher.matches()) {
				throw new InvalidSPDXAnalysisException("Element object URI does not follow the SPDX V2.X required pattern" + 
						SpdxConstantsCompatV2.EXTERNAL_SPDX_ELEMENT_URI_PATTERN);
			}
			return SpdxModelFactoryCompatV2.getModelObjectV2(modelStore, matcher.group(1), matcher.group(2), type, copyManager, create);
		} else if (ExtractedLicenseInfo.class.isAssignableFrom(typeClass)) {
			if (IdType.Anonymous.equals(modelStore.getIdType(objectUri))) {
				logger.error("Extracted licenses must not be anonomous types - missing ID for "+objectUri);
				throw new InvalidSPDXAnalysisException("Extracted licenses must not be anonomous types - missing ID for "+objectUri);
			}
			Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_EXTRACTED_LICENSE_URI_PATTERN.matcher(objectUri);
			if (!matcher.matches()) {
				throw new InvalidSPDXAnalysisException("ExtractedLicenseInfo object URI does not follow the SPDX V2.X required pattern" + 
						SpdxConstantsCompatV2.EXTERNAL_EXTRACTED_LICENSE_URI_PATTERN);
			}
			return SpdxModelFactoryCompatV2.getModelObjectV2(modelStore, matcher.group(1), matcher.group(2), type, copyManager, create);
		} else {
			String documentUri;
			String id;
			if (objectUri.contains("#")) {
				int index = objectUri.lastIndexOf('#');
				documentUri = objectUri.substring(0, index);
				id = objectUri.substring(index + 1);
			} else if (Objects.nonNull(prefix)) {
				documentUri = prefix.endsWith("#") ? prefix.substring(0, prefix.length()-1) : prefix;
				id = objectUri;
			} else {
				documentUri = DefaultModelStore.getDefaultDocumentUri();
				id = objectUri;
			}
			return SpdxModelFactoryCompatV2.getModelObjectV2(modelStore, documentUri, id, type, copyManager, create);
		}
	}

	@Override
	public Map<String, Class<?>> getTypeToClassMap() {
		return SpdxModelFactoryCompatV2.SPDX_TYPE_TO_CLASS_V2;
	}

	@Override
	public boolean canBeExternal(Class<?> clazz) {
		return AnyLicenseInfo.class.isAssignableFrom(clazz) || Element.class.isAssignableFrom(clazz);
	}

}
