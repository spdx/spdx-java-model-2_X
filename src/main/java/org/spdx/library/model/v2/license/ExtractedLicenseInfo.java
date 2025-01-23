/**
 * Copyright (c) 2011, 2019 Source Auditor Inc.
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
 *
*/
package org.spdx.library.model.v2.license;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import org.spdx.core.CoreModelObject;
import org.spdx.core.DefaultModelStore;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;
import org.spdx.library.model.v2.SpdxVerificationHelper;
import org.spdx.licenseTemplate.LicenseTextHelper;
import org.spdx.storage.IModelStore;
import org.spdx.storage.IModelStore.IdType;

/**
 * An ExtractedLicensingInfo represents a license or licensing notice that was found in the package.
 *
 * Any license text that is recognized as a license may be represented as a License 
 * rather than an ExtractedLicensingInfo.
 *
 * @author Gary O'Neall
 */
public class ExtractedLicenseInfo extends AbstractExtractedLicenseInfo {
	
	public static final String UNINITIALIZED_LICENSE_TEXT = "[Initialized with license Parser.  The actual license text is not available]";
	
	public ExtractedLicenseInfo() throws InvalidSPDXAnalysisException {
		super(DefaultModelStore.getDefaultDocumentUri() + "#" + DefaultModelStore.getDefaultModelStore().getNextId(IdType.LicenseRef));
	}
	
	/**
	 * @param id identifier
	 * @throws InvalidSPDXAnalysisException
	 */
	public ExtractedLicenseInfo(String id) throws InvalidSPDXAnalysisException {
		super(id);
	}

	/**
	 * Create a new ExtractedLicenseInfo object
	 * @param modelStore container which includes the license
	 * @param documentUri URI for the SPDX document containing the license
	 * @param id identifier for the license
	 * @param copyManager if non-null, allows for copying of any properties set which use other model stores or document URI's
	 * @param create if true, create the license if it does not exist
	 * @throws InvalidSPDXAnalysisException 
	 */
	public ExtractedLicenseInfo(IModelStore modelStore, String documentUri, String id, 
			@Nullable IModelCopyManager copyManager, boolean create)
			throws InvalidSPDXAnalysisException {
		super(modelStore, documentUri, id, copyManager, create);
	}
	
	/**
	 * Create a new ExtractedLicenseInfo using the ID and text
	 * @param id identifier
	 * @param text license text
	 * @throws InvalidSPDXAnalysisException 
	 */
	public ExtractedLicenseInfo(String id, String text) throws InvalidSPDXAnalysisException {
		super(id);
		this.setExtractedText(text);
	}

	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#getType()
	 */
	@Override
	public String getType() {
		return SpdxConstantsCompatV2.CLASS_SPDX_EXTRACTED_LICENSING_INFO;
	}
	
	/**
	 * @return the text
	 * @throws InvalidSPDXAnalysisException 
	 */
	public String getExtractedText() throws InvalidSPDXAnalysisException {
		Optional<String> o = getStringPropertyValue(SpdxConstantsCompatV2.PROP_EXTRACTED_TEXT);
		if (o.isPresent()) {
			return o.get();
		} else {
			return "";
		}
	}

	/**
	 * @param text the text to set
	 * @throws InvalidSPDXAnalysisException 
	 */
	public void setExtractedText(String text) throws InvalidSPDXAnalysisException {
		setPropertyValue(SpdxConstantsCompatV2.PROP_EXTRACTED_TEXT, text);
	}

	/**
	 * @return
	 */
	@Override
    protected List<String> _verify(Set<String> verifiedIds, String specVersion) {
		List<String> retval = new ArrayList<>();
		String id = this.getLicenseId();
		if (id == null || id.isEmpty()) {
			retval.add("Missing required license ID");
		} else {
			String idError = SpdxVerificationHelper.verifyNonStdLicenseId(id);
			if (idError != null && !idError.isEmpty()) {
				retval.add(idError);
			}
		}
		try {
			String licenseText = this.getExtractedText();
			if (licenseText == null || licenseText.isEmpty()) {
				retval.add("Missing required license text for " + id);
			}
			if (UNINITIALIZED_LICENSE_TEXT.equals(licenseText)) {
				retval.add("License not found for " + id);
			}
		} catch (InvalidSPDXAnalysisException ex) {
			retval.add("Unable to fetch license text: "+ex.getMessage());
		}
		return retval;
	}
	
	@Override
	public boolean equivalent(CoreModelObject compare) throws InvalidSPDXAnalysisException {
		return equivalent(compare, false);
	}
	
	@Override
	public boolean equivalent(CoreModelObject compare, boolean ignoreRelatedItems) throws InvalidSPDXAnalysisException {
		if (compare instanceof ExtractedLicenseInfo) {
			// Only test for the text - other fields do not need to equal to be considered equivalent
			return LicenseTextHelper.isLicenseTextEquivalent(this.getExtractedText(), ((ExtractedLicenseInfo)compare).getExtractedText());
		} else {
			return false;
		}
	}
}
