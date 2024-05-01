/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.library.model.v2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spdx.core.CoreModelObject;
import org.spdx.core.DefaultModelStore;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.ISpdxModelInfo;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.library.model.v2.enumerations.SpdxEnumFactoryCompatV2;
import org.spdx.library.model.v2.license.ExternalExtractedLicenseInfo;
import org.spdx.library.model.v2.license.ExtractedLicenseInfo;
import org.spdx.library.model.v2.license.SpdxListedLicense;
import org.spdx.library.model.v2.license.SpdxListedLicenseException;
import org.spdx.library.model.v2.license.SpdxNoAssertionLicense;
import org.spdx.library.model.v2.license.SpdxNoneLicense;
import org.spdx.storage.IModelStore;
import org.spdx.storage.IModelStore.IdType;

/**
 * @author Gary O'Neall
 * 
 * Compatible model info for all supported spec version 2 (2.0, 2.1, 2.2, 2.3)
 *
 */
public class SpdxModelInfoV2_X implements ISpdxModelInfo {
	
	static final Logger logger = LoggerFactory.getLogger(SpdxModelInfoV2_X.class);
	
	static final Map<String, Object> URI_TO_INDIVIDUAL;
	
	static {
		Map<String, Object> temp = new HashMap<>();
		try {
			temp.put(SpdxConstantsCompatV2.URI_VALUE_NONE, new SpdxNoneLicense());
		} catch (InvalidSPDXAnalysisException e) {
			logger.error("Unable to create a None license");
		} //NOTE: There is also a NoneElement, but this is rarely used
		try {
			temp.put(SpdxConstantsCompatV2.URI_VALUE_NOASSERTION, new SpdxNoAssertionLicense());
		} catch (InvalidSPDXAnalysisException e) {
			logger.error("Unable to create a NoAssertion license");
		} //NOTE: There is also a NoAssertionElement, but this is rarely used
		URI_TO_INDIVIDUAL = Collections.unmodifiableMap(temp);
	}

	@Override
	public @Nullable CoreModelObject createExternalElement(IModelStore store, String uri,
			IModelCopyManager copyManager, String specVersion)
			throws InvalidSPDXAnalysisException {
		Matcher licenseMatcher = SpdxConstantsCompatV2.EXTERNAL_SPDX_ELEMENT_URI_PATTERN.matcher(uri);
		if (licenseMatcher.matches()) {
			return new ExternalExtractedLicenseInfo(store, licenseMatcher.group(1), licenseMatcher.group(2), copyManager, true);
		} 
		Matcher elementMatcher = SpdxConstantsCompatV2.EXTERNAL_EXTRACTED_LICENSE_URI_PATTERN.matcher(uri);
		if (elementMatcher.matches()) {
			return new ExternalSpdxElement(store, elementMatcher.group(1), elementMatcher.group(2), copyManager, true);
		} else return null;
	}

	@Override
	public List<String> getSpecVersions() {
		return Arrays.asList(new String[] {"SPDX-2.0", "SPDX-2.1", "SPDX-2.2", "SPDX-2.3"});
	}

	@Override
	public Map<String, Enum<?>> getUriToEnumMap() {
		return SpdxEnumFactoryCompatV2.uriToEnum;
	}

	@Override
	public Map<String, Object> getUriToIndividualMap() {
		return URI_TO_INDIVIDUAL;
	}

	@Override
	public CoreModelObject createModelObject(IModelStore modelStore,
			String objectUri, String type, IModelCopyManager copyManager,
			String specVersion, boolean create) throws InvalidSPDXAnalysisException {
		if (!SpdxModelFactory.SPDX_TYPE_TO_CLASS_V2.containsKey(type)) {
			logger.error(type+" not a supported type for SPDX spec version 2.X");
			throw new InvalidSPDXAnalysisException(type+" not a supported type for SPDX spec version 2.X");
		}
		Class<?> typeClass = SpdxModelFactory.SPDX_TYPE_TO_CLASS_V2.get(type);
		if (SpdxListedLicense.class.isAssignableFrom(typeClass)) {
			// check that the URI is a listed license URI
			if (!objectUri.startsWith(SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX)) {
				logger.error("'" + objectUri + "' Listed license URI does not start with " +
								SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX + ".");
				throw new InvalidSPDXAnalysisException("'" + objectUri + "' Listed license URI does not start with " +
						SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX + ".");
			}
			String id = objectUri.substring(SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX.length());
			return SpdxModelFactory.getModelObjectV2(modelStore, SpdxConstantsCompatV2.LISTED_LICENSE_URL, 
					id, SpdxConstantsCompatV2.CLASS_SPDX_LISTED_LICENSE, copyManager, true);
		} else if (SpdxListedLicenseException.class.isAssignableFrom(typeClass)) {
			// check that the URI is a listed license URI
			if (!objectUri.startsWith(SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX)) {
				logger.error("'" + objectUri + "' Listed license exception URI does not start with " +
								SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX + ".");
				throw new InvalidSPDXAnalysisException("'" + objectUri + "' Listed exception license URI does not start with " +
						SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX + ".");
			}
			String id = objectUri.substring(SpdxConstantsCompatV2.LISTED_LICENSE_NAMESPACE_PREFIX.length());
			return SpdxModelFactory.getModelObjectV2(modelStore, SpdxConstantsCompatV2.LISTED_LICENSE_URL, 
					id, SpdxConstantsCompatV2.CLASS_SPDX_LISTED_LICENSE_EXCEPTION, copyManager, true);
		} else if (SpdxElement.class.isAssignableFrom(typeClass)) {
			//TODO: Do we want to check for external references if (modelStore.getExternalReferenceMap(objectUri) != null && ??)
			if (IdType.Anonymous.equals(modelStore.getIdType(objectUri))) {
				logger.error("SPDX elements must not be anonomous types - missing ID for "+objectUri);
				throw new InvalidSPDXAnalysisException("SPDX elements must not be anonomous types - missing ID for "+objectUri);
			}
			// Note that the EXTERNAL_SPDX_ELEMENT_URI_PATTERN also matches the full URI for the object
			Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_SPDX_ELEMENT_URI_PATTERN.matcher(objectUri);
			if (!matcher.matches()) {
				throw new InvalidSPDXAnalysisException("Element object URI does not follow the SPDX V2.X required pattern" + 
						SpdxConstantsCompatV2.EXTERNAL_SPDX_ELEMENT_URI_PATTERN);
			}
			return SpdxModelFactory.getModelObjectV2(modelStore, matcher.group(1), matcher.group(2), type, copyManager, create);
		} else if (ExtractedLicenseInfo.class.isAssignableFrom(typeClass)) {
			//TODO: Do we want to check for external references? if (modelStore.getExternalReferenceMap(objectUri) != null && ??)
			if (IdType.Anonymous.equals(modelStore.getIdType(objectUri))) {
				logger.error("Extracted licenses must not be anonomous types - missing ID for "+objectUri);
				throw new InvalidSPDXAnalysisException("Extracted licenses must not be anonomous types - missing ID for "+objectUri);
			}
			Matcher matcher = SpdxConstantsCompatV2.EXTERNAL_EXTRACTED_LICENSE_URI_PATTERN.matcher(objectUri);
			if (!matcher.matches()) {
				throw new InvalidSPDXAnalysisException("ExtractedLicenseInfo object URI does not follow the SPDX V2.X required pattern" + 
						SpdxConstantsCompatV2.EXTERNAL_EXTRACTED_LICENSE_URI_PATTERN);
			}
			return SpdxModelFactory.getModelObjectV2(modelStore, matcher.group(1), matcher.group(2), type, copyManager, create);
		} else {
			String documentUri;
			String id;
			if (objectUri.contains("#")) {
				int index = objectUri.lastIndexOf('#');
				documentUri = objectUri.substring(0, index);
				id = objectUri.substring(index + 1);
				// TODO: Test to make sure I'm not off by one
			} else {
				documentUri = DefaultModelStore.getDefaultDocumentUri();
				id = objectUri;
			}
			return SpdxModelFactory.getModelObjectV2(modelStore, documentUri, id, type, copyManager, create);
		}
	}

	@Override
	public Map<String, Class<?>> getTypeToClassMap() {
		return SpdxModelFactory.SPDX_TYPE_TO_CLASS_V2;
	}

}
