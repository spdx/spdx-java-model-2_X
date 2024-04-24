/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.library.model.v2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import org.spdx.core.IExternalMap;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.ISpdxModelInfo;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.library.model.v2.enumerations.SpdxEnumFactoryCompatV2;
import org.spdx.storage.IModelStore;

/**
 * @author Gary O'Neall
 * 
 * Compatible model info for all supported spec version 2 (2.0, 2.1, 2.2, 2.3)
 *
 */
public class SpdxModelInfoV2_X implements ISpdxModelInfo {

	/**
	 * 
	 */
	public SpdxModelInfoV2_X() {
		
	}

	public Object createExternalElement(IModelStore store, String uri,
			IModelCopyManager copyManager, IExternalMap externalMap,
			String documentUri)
			throws InvalidSPDXAnalysisException {
		if (Objects.isNull(documentUri)) {
			throw new InvalidSPDXAnalysisException("Document URI is required")
		}
		if (SpdxConstantsCompatV2.EXTERNAL_SPDX_ELEMENT_URI_PATTERN.matcher(uri).matches()) {
			return ExternalSpdxElement.uriToExternalSpdxElement(uri, store, documentUri, copyManager);
		} else if (SpdxConstantsCompatV2.EXTERNAL_EXTRACTED_LICENSE_URI_PATTERN.matcher(uri).matches()) {
			return ExternalExtractedLicenseInfo.uriToExternalExtractedLicense(uri, store, documentUri, copyManager);
		} else if (SpdxConstantsCompatV2.REFERENCE_TYPE_URI_PATTERN.matcher(uri).matches()) {
			return new ReferenceType(this);
		} else return null;
	}

	public List<String> getSpecVersions() {
		return Arrays.asList(new String[] {"SPDX-2.0", "SPDX-2.1", "SPDX-2.2", "SPDX-2.3"});
	}

	public Map<String, Enum<?>> getUriToEnumMap() {
		return SpdxEnumFactoryCompatV2.uriToEnum;
	}

	public Map<String, ISpdxModelInfo> getUriToIndividualMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
