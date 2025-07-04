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
package org.spdx.library.model.v2.pointer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.spdx.core.IModelCopyManager;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;
import org.spdx.storage.IModelStore;

/**
 * Byte offset pointer per RDF 2.3.2.2 ByteOffsetPointer Class
 *
 * @author Gary O'Neall
 */
public class ByteOffsetPointer extends SinglePointer {

	/**
	 * @throws InvalidSPDXAnalysisException
	 */
	public ByteOffsetPointer() throws InvalidSPDXAnalysisException {
		super();
	}

	/**
	 * @param id identifier for the ByteOffsetPointer
	 * @throws InvalidSPDXAnalysisException
	 */
	public ByteOffsetPointer(String id) throws InvalidSPDXAnalysisException {
		super(id);
	}

	/**
	 * @param modelStore container which includes the ByteOffsetPointer
	 * @param documentUri URI for the SPDX document containing the ByteOffsetPointer
	 * @param id identifier for the ByteOffsetPointer
	 * @param copyManager if non-null, allows for copying of any properties set which use other model stores or document URI's
	 * @param create if true, create the license if it does not exist
	 * @throws InvalidSPDXAnalysisException
	 */
	public ByteOffsetPointer(IModelStore modelStore, String documentUri, String id, IModelCopyManager copyManager,
			boolean create) throws InvalidSPDXAnalysisException {
		super(modelStore, documentUri, id, copyManager, create);
	}

	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#getType()
	 */
	@Override
	public String getType() {
		return SpdxConstantsCompatV2.CLASS_POINTER_BYTE_OFFSET_POINTER;
	}
	
	/**
	 * @return the offset, -1 if no offset is stored
	 */
	public int getOffset() throws InvalidSPDXAnalysisException {
		Optional<Integer> retval = getIntegerPropertyValue(SpdxConstantsCompatV2.PROP_POINTER_OFFSET);
		if (!retval.isPresent()) {
			return -1;
		}
		return retval.get();
	}
	
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Integer offset) throws InvalidSPDXAnalysisException {
		if (strict) {
			if (Objects.isNull(offset) || offset < 0) {
				throw new InvalidSPDXAnalysisException("Can not set required offset to null or less than zero");
			}
		}
		setPropertyValue(SpdxConstantsCompatV2.PROP_POINTER_OFFSET, offset);
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#_verify(java.util.List)
	 */
	@Override
	protected List<String> _verify(Set<String> verifiedIds, String specVersion) {
		List<String> retval = super._verify(verifiedIds, specVersion);
		int offset;
		try {
			offset = getOffset();
			if (offset == -1) {
				retval.add("Missing byte offset offset value");
			} else if (offset < 0) {
				retval.add("Offset must not be negative for a byte pointer: "+offset);
			}
		} catch (InvalidSPDXAnalysisException e) {
			retval.add("Error getting offset: "+e.getMessage());
		}

		return retval;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SinglePointer o) {
		if (o == null) {
			return 1;
		}
		int retval = compareReferences(o);
		if (retval != 0) {
			return retval;
		}
		if (!(o instanceof ByteOffsetPointer)) {
			return 1;
		}

		int compByteOffset;
		try {
			compByteOffset = ((ByteOffsetPointer)o).getOffset();
		} catch (InvalidSPDXAnalysisException e) {
			logger.warn("Error getting compare offset",e);
			compByteOffset = -1;
		}
		Integer myOffset;
		try {
			myOffset = getOffset();
		} catch (InvalidSPDXAnalysisException e) {
			logger.warn("Error getting offset",e);
			myOffset = -1;
		}
		return myOffset.compareTo(compByteOffset);
	}
	
	@Override
	public String toString() {
		int offset;
		try {
			offset = getOffset();
			return "byte offset " +offset;
		} catch (InvalidSPDXAnalysisException e) {
			logger.warn("Error getting offset",e);
			return "Unknown byte offset";
		}
	}
}
