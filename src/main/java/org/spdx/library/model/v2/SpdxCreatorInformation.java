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

import org.spdx.core.IModelCopyManager;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.storage.IModelStore;

/**
 * Provides the necessary information for forward and backward compatibility for processing tools.
 *
 * @author Gary O'Neall
 */
public class SpdxCreatorInformation extends ModelObjectV2 {

	/**
	 * @throws InvalidSPDXAnalysisException
	 */
	public SpdxCreatorInformation() throws InvalidSPDXAnalysisException {
		super();
	}

	/**
	 * @param id identifier
	 * @throws InvalidSPDXAnalysisException
	 */
	public SpdxCreatorInformation(String id) throws InvalidSPDXAnalysisException {
		super(id);
	}

	/**
	 * @param modelStore container which includes the model data
	 * @param documentUri URI for the SPDX document containing the model data
	 * @param id identifier
	 * @param copyManager if non-null, allows for copying of any properties set which use other model stores or document URI's
	 * @param create if true, create the license if it does not exist
	 * @throws InvalidSPDXAnalysisException
	 */
	public SpdxCreatorInformation(IModelStore modelStore, String documentUri, String id, IModelCopyManager copyManager,
			boolean create)	throws InvalidSPDXAnalysisException {
		super(modelStore, documentUri, id, copyManager, create);
	}
	
	/**
	 * @return Creators Identify who (or what, in the case of a tool) created the SPDX file.  If the SPDX file was created by an individual, indicate the person's name. 
	 * @throws InvalidSPDXAnalysisException
	 */
	public Collection<String> getCreators() throws InvalidSPDXAnalysisException {
		return this.getStringCollection(SpdxConstantsCompatV2.PROP_CREATION_CREATOR);
	}
	
	/**
	 * @return An optional field for creators of the SPDX file to provide the version of the SPDX License List used when the SPDX file was created.
	 * @throws InvalidSPDXAnalysisException
	 */
	public Optional<String> getLicenseListVersion() throws InvalidSPDXAnalysisException {
		return getStringPropertyValue(SpdxConstantsCompatV2.PROP_LICENSE_LIST_VERSION);
	}
	
	/**
	 * @param licenseListVersion An optional field for creators of the SPDX file to provide the version of the SPDX License List used when the SPDX file was created.
	 * @return this for building more options
	 * @throws InvalidSPDXAnalysisException
	 */
	public SpdxCreatorInformation setLicenseListVersion(String licenseListVersion) throws InvalidSPDXAnalysisException {
		setPropertyValue(SpdxConstantsCompatV2.PROP_LICENSE_LIST_VERSION, licenseListVersion);
		return this;
	}

	
	/**
	 * @return the comment
	 */
	public Optional<String> getComment() throws InvalidSPDXAnalysisException {
		return getStringPropertyValue(SpdxConstantsCompatV2.RDFS_PROP_COMMENT);
	}
	
	/**
	 * @param comment
	 * @return this for building more options
	 * @throws InvalidSPDXAnalysisException
	 */
	public SpdxCreatorInformation setComment(String comment) throws InvalidSPDXAnalysisException {
		setPropertyValue(SpdxConstantsCompatV2.RDFS_PROP_COMMENT, comment);
		return this;
	}
	
	/** 
	 * @return When the SPDX file was originally created. The date is to be specified according to combined date and time in UTC format as specified in ISO 8601 standard. 
	 * @throws InvalidSPDXAnalysisException
	 */
	public String getCreated() throws InvalidSPDXAnalysisException {
		Optional<String> retval = getStringPropertyValue(SpdxConstantsCompatV2.PROP_CREATION_CREATED);
		if (retval.isPresent()) {
			return retval.get();
		} else {
			logger.warn("Missing created date");
			return "";
		}
	}
	
	/**
	 * @param created When the SPDX file was originally created. The date is to be specified according to combined date and time in UTC format as specified in ISO 8601 standard.
	 * @return this for building more options
	 * @throws InvalidSPDXAnalysisException
	 */
	public SpdxCreatorInformation setCreated(String created) throws InvalidSPDXAnalysisException {
		if (strict) {
			if (Objects.isNull(created)) {
				throw new InvalidSPDXAnalysisException("Can not set required created date to null");
			}
			String verify = SpdxVerificationHelper.verifyDate(created);
			if (Objects.nonNull(verify) && !verify.isEmpty()) {
				throw new InvalidSPDXAnalysisException(verify);
			}
		}
		setPropertyValue(SpdxConstantsCompatV2.PROP_CREATION_CREATED, created);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#getType()
	 */
	@Override
	public String getType() {
		return SpdxConstantsCompatV2.CLASS_SPDX_CREATION_INFO;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		try {
			for (String creator:getCreators()) {
				if (!first) {
					sb.append(", ");
				}
				sb.append(creator);
				first = false;
			}
		} catch (InvalidSPDXAnalysisException e) {
			logger.warn("Error getting creators",e);
		}
		String created;
		try {
			created = getCreated();
		} catch (InvalidSPDXAnalysisException e) {
			logger.warn("Error getting created",e);
			created = "";
		}
		sb.append("; Created on ");
		sb.append(created);
		Optional<String> licenseListVersion;
		try {
			licenseListVersion = getLicenseListVersion();
		} catch (InvalidSPDXAnalysisException e) {
			logger.warn("Error getting licenseListVersion",e);
			licenseListVersion = Optional.empty();
		}
		if (licenseListVersion.isPresent()) {
			sb.append("; License List Version=");
			sb.append(licenseListVersion.get());
		}
		Optional<String> comment;
		try {
			comment = getComment();
		} catch (InvalidSPDXAnalysisException e) {
			logger.warn("Error getting comment",e);
			comment = Optional.empty();
		}
		if (comment.isPresent()) {
			sb.append("; Comment: ");
			sb.append(comment.get());
		}
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#_verify(java.util.List)
	 */
	@Override
	protected List<String> _verify(Set<String> verifiedIds, String specVersion) {
		List<String> retval = new ArrayList<>();
		try {
			int numCreators = 0;
			for (String creator:getCreators()) {
				String verify = SpdxVerificationHelper.verifyCreator(creator);
				if (verify != null) {
					retval.add(verify);
				}
				numCreators++;
			}
			if (numCreators == 0) {
				retval.add("Missing required creators");
			}
		} catch (InvalidSPDXAnalysisException e) {
			retval.add("Error getting creators: "+e.getMessage());
		}
		try {
			String creationDate = this.getCreated();
			if (creationDate.isEmpty()) {
				retval.add("Missing required created date");
			} else {
				String verify = SpdxVerificationHelper.verifyDate(creationDate);
				if (verify != null) {
					retval.add(verify);
				}
			}
		} catch (InvalidSPDXAnalysisException e) {
			retval.add("Error getting creation date: "+e.getMessage());
		}
		// ListList Versions
		try {
			Optional<String> licenseListVersion = this.getLicenseListVersion();
			if (licenseListVersion.isPresent()) {
				String verify =  verifyLicenseListVersion(licenseListVersion.get());
				if (verify != null) {
					retval.add(verify);
				}
			}
		} catch (InvalidSPDXAnalysisException e) {
			retval.add("Error getting license list version: "+e.getMessage());
		}
		return retval;
	}
	
	/**
	 * @param version
	 * @return
	 */
	private @Nullable String verifyLicenseListVersion(String version) {
		// Currently, there is no rules for the format of a version
		if (Objects.isNull(version)) {
		    return null;
		} else {
		    if (SpdxConstantsCompatV2.LICENSE_LIST_VERSION_PATTERN.matcher(version).matches()) {
		        return null;
		    } else {
		        return "License list version does not match the pattern M.N or M.N.P";
		    }
		}
	}

}
