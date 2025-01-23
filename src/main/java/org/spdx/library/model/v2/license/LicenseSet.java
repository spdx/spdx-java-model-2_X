/**
 * Copyright (c) 2011 Source Auditor Inc.
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

import org.spdx.core.DefaultModelStore;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;
import org.spdx.storage.IModelStore;
import org.spdx.storage.IModelStore.IdType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * A specific form of license information where there is a set of licenses
 * represented
 *
 * @author Gary O'Neall
 */
public abstract class LicenseSet extends AnyLicenseInfo {
	
	//TODO: Check for recursive references when adding or setting license ID's - currently, this ends badly in an infinite loop on toString
	
	Collection<AnyLicenseInfo> members;
	
	
	/**
	 * @throws InvalidSPDXAnalysisException
	 */
	public LicenseSet() throws InvalidSPDXAnalysisException {
		this(DefaultModelStore.getDefaultModelStore().getNextId(IdType.Anonymous));
	}

	/**
	 * @param id identifier
	 * @throws InvalidSPDXAnalysisException
	 */
	public LicenseSet(String id) throws InvalidSPDXAnalysisException {
		this(DefaultModelStore.getDefaultModelStore(), DefaultModelStore.getDefaultDocumentUri(), id, 
				DefaultModelStore.getDefaultCopyManager(), true);
	}

	/**
	 * @param modelStore container which includes the model data
	 * @param documentUri URI for the SPDX document containing the model data
	 * @param id identifier
	 * @param copyManager if non-null, allows for copying of any properties set which use other model stores or document URI's
	 * @param create if true, create the license if it does not exist
	 * @throws InvalidSPDXAnalysisException
	 */
	@SuppressWarnings("unchecked")
	LicenseSet(IModelStore modelStore, String documentUri, String id, 
			@Nullable IModelCopyManager copyManager, boolean create)
			throws InvalidSPDXAnalysisException {
		super(modelStore, documentUri, id, copyManager, create);
		members = (Collection<AnyLicenseInfo>)(Collection<?>)getObjectPropertyValueSet(SpdxConstantsCompatV2.PROP_LICENSE_SET_MEMEBER, AnyLicenseInfo.class);
	}

	/**
	 * Sets the members of the license set.  Clears any previous members
	 * @param licenseInfos New members for the set
	 * @throws InvalidSPDXAnalysisException
	 */
	public void setMembers(Collection<AnyLicenseInfo> licenseInfos) throws InvalidSPDXAnalysisException {
		setPropertyValue(SpdxConstantsCompatV2.PROP_LICENSE_SET_MEMEBER, licenseInfos);
	}
	
	/**
	 * @return Members of the license set
	 * @throws InvalidSPDXAnalysisException 
	 */
	public Collection<AnyLicenseInfo> getMembers() throws InvalidSPDXAnalysisException {
		return members;
	}
	
	/**
	 * Adds a member to the set
	 * @param member
	 * @throws InvalidSPDXAnalysisException
	 */
	public void addMember(AnyLicenseInfo member) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(member, "Member can not be null");
		members.add(member);
	}
	
	public void removeMember(AnyLicenseInfo member) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(member, "Member can not be null");
		members.remove(member);
	}

	@Override
	protected List<String> _verify(Set<String> verifiedIds, String specVersion) {
		List<String> retval = new ArrayList<>();
		Iterator<AnyLicenseInfo> iter;
		try {
			iter = getMembers().iterator();
			while (iter.hasNext()) {
				retval.addAll(iter.next().verify(verifiedIds, specVersion));
			}
		} catch (InvalidSPDXAnalysisException e) {
			retval.add("Exception getting license set members: "+e.getMessage());
		}
		return retval;
	}
}
