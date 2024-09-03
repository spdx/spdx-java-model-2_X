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

import java.util.Collection;
import java.util.Optional;

import org.spdx.core.CoreModelObject;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;
import org.spdx.licenseTemplate.LicenseTemplateRuleException;
import org.spdx.licenseTemplate.SpdxLicenseTemplateHelper;
import org.spdx.storage.IModelStore;

/**
 * Represents a License Exception present on the SPDX License List
 * 
 * @author Gary O'Neall
 *
 */
public class ListedLicenseException extends LicenseException {

	/**
@param modelStore container which includes the model data
	 * @param documentUri URI for the SPDX document containing the model data
	 * @param id identifier
	 * @param copyManager if non-null, allows for copying of any properties set which use other model stores or document URI's
	 * @param create if true, create the license if it does not exist
	 * @throws InvalidSPDXAnalysisException
	 */
	public ListedLicenseException(IModelStore modelStore, String documentUri, String id, IModelCopyManager copyManager,
			boolean create) throws InvalidSPDXAnalysisException {
		super(modelStore, documentUri, id, copyManager, create);
	}

	/**
	 * @param id identifier
	 * @param name
	 * @param text
	 * @param seeAlso
	 * @param comment
	 * @throws InvalidSPDXAnalysisException
	 */
	public ListedLicenseException(String id, String name, String text, Collection<String> seeAlso, String comment)
			throws InvalidSPDXAnalysisException {
		super(id, name, text, seeAlso, comment);
	}

	/**
	 * @param id identifier
	 * @param name
	 * @param text
	 * @param template
	 * @param seeAlso
	 * @param comment
	 * @throws InvalidSPDXAnalysisException
	 */
	public ListedLicenseException(String id, String name, String text, String template, Collection<String> seeAlso,
			String comment) throws InvalidSPDXAnalysisException {
		super(id, name, text, template, seeAlso, comment);
	}

	/**
	 * @param id identifier
	 * @param name
	 * @param text
	 * @throws InvalidSPDXAnalysisException
	 */
	public ListedLicenseException(String id, String name, String text) throws InvalidSPDXAnalysisException {
		super(id, name, text);
	}
	
	@Override 
	public String getType() {
		return SpdxConstantsCompatV2.CLASS_SPDX_LISTED_LICENSE_EXCEPTION;
	}
	
	/**
	 * @param exceptionTextHtml
	 * @throws InvalidSPDXAnalysisException
	 */
	public void setExceptionTextHtml(String exceptionTextHtml) throws InvalidSPDXAnalysisException {
		setPropertyValue(SpdxConstantsCompatV2.PROP_EXCEPTION_TEXT_HTML, exceptionTextHtml);
	}
	
	/**
	 * @return HTML form of the exception text either from a stored property or generated from the template or text
	 * @throws InvalidSPDXAnalysisException
	 */
	public String getExceptionTextHtml() throws InvalidSPDXAnalysisException {
		Optional<String> exceptionTextHtml = getStringPropertyValue(SpdxConstantsCompatV2.PROP_EXCEPTION_TEXT_HTML);
		if (exceptionTextHtml.isPresent()) {
			return exceptionTextHtml.get();
		} else {
			Optional<String> templateText = getStringPropertyValue(SpdxConstantsCompatV2.PROP_EXCEPTION_TEMPLATE);
			if (templateText.isPresent()) {
				try {
					return SpdxLicenseTemplateHelper.templateTextToHtml(templateText.get());
				} catch(LicenseTemplateRuleException ex) {
					throw new InvalidSPDXAnalysisException("Invalid license rule found in exception text for exception "+getName()+":"+ex.getMessage());
				}
			} else {
				Optional<String> exceptionText = getStringPropertyValue(SpdxConstantsCompatV2.PROP_EXCEPTION_TEXT);
				if (exceptionText.isPresent()) {
					return SpdxLicenseTemplateHelper.formatEscapeHTML(exceptionText.get());
				} else {
					return "";
				}
			}
		}
	}
	
	   @Override
	    public boolean equivalent(CoreModelObject compare, boolean ignoreRelatedElements) throws InvalidSPDXAnalysisException {
	        if (compare instanceof ListedLicenseException) {
	            return this.getId().equals(((ListedLicenseException)compare).getId()); // for listed license, the license ID is the only thing that matters
	        } else {
	            return super.equivalent(compare, ignoreRelatedElements);
	        }
	    }

}
