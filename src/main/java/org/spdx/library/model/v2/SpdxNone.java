/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.library.model.v2;

import org.spdx.core.IndividualUriValue;

/**
 * Represents either a NoneLicense OR a NoneElement
 * 
 * The correct class should be replaced by the caller based on the context.
 * 
 * @author Gary O'Neall
 */
public class SpdxNone implements IndividualUriValue {

	@Override
	public String getIndividualURI() {
		return SpdxConstantsCompatV2.URI_VALUE_NONE;
	}

}
