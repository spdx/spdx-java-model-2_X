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
package org.spdx.library.model.v2.enumerations;

import org.spdx.core.IndividualUriValue;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;

/**
 * Annotation types for the Annotation Class
 *
 * @author Gary O'Neall
 */
public enum AnnotationType implements IndividualUriValue {
	
	OTHER("annotationType_other"),
	REVIEW("annotationType_review"), 
	MISSING("not_allowed");
	
	private String longName;
	
	private AnnotationType(String longName) {
		this.longName = longName;
	}
	@Override
	public String getIndividualURI() {
		return getNameSpace() + getLongName();
	}

	public String getLongName() {
		return longName;
	}

	public String getNameSpace() {
		return SpdxConstantsCompatV2.SPDX_NAMESPACE;
	}

}
