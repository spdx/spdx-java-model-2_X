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

import javax.annotation.Nullable;

import org.spdx.core.DefaultModelStore;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.storage.IModelStore;
import org.spdx.storage.IModelStore.IdType;

/**
 * Generic SPDX element - primarily used for testing
 *
 * @author Gary O'Neall
 */
public class GenericSpdxElement extends SpdxElement {

	public static final String GENERIC_SPDX_ELEMENT_TYPE = "GenericSpdxElement";

	/**
	 * @throws InvalidSPDXAnalysisException
	 */
	public GenericSpdxElement() throws InvalidSPDXAnalysisException {
		super(DefaultModelStore.getDefaultModelStore().getNextId(IdType.SpdxId));
	}

	/**
	 * @param id identifier
	 * @throws InvalidSPDXAnalysisException
	 */
	public GenericSpdxElement(String id) throws InvalidSPDXAnalysisException {
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
	public GenericSpdxElement(IModelStore modelStore, String documentUri, String id, 
			@Nullable IModelCopyManager copyManager, boolean create)
			throws InvalidSPDXAnalysisException {
		super(modelStore, documentUri, id, copyManager, create);
	}

	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#getType()
	 */
	@Override
	public String getType() {
		return GENERIC_SPDX_ELEMENT_TYPE;
	}

}
