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
package org.spdx.library.model.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.spdx.core.IndividualUriValue;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.SimpleUriValue;
import org.spdx.storage.IModelStore;
import org.spdx.storage.NullModelStore;

/**
 * Type of SpdxElement which is a constant unmodifiable element
 *
 * @author Gary O'Neall
 */
public abstract class SpdxConstantElement extends SpdxElement implements IndividualUriValue {

	private Collection<Annotation> annotations = Collections.unmodifiableCollection(new ArrayList<>());
	private Collection<Relationship> relationships = Collections.unmodifiableCollection(new ArrayList<>());
	
	public SpdxConstantElement(String id) throws InvalidSPDXAnalysisException {
		super(new NullModelStore(), "https://spdx.org/rdf/2.3.1", id, null, true);
	}
	
	/**
	 * @param modelStore where the model is stored
	 * @param documentUri Unique document URI
	 * @param id ID for the constant element
	 * @throws InvalidSPDXAnalysisException
	 */
	public SpdxConstantElement(IModelStore modelStore, String documentUri, String id)
			throws InvalidSPDXAnalysisException {
		super(modelStore, documentUri, id, null, true);
	}
	
	@Override
	public boolean isExternal() {
		return true;
	}

	@Override
	protected List<String> _verify(Set<String> verifiedIds, String specVersion) {
		return new ArrayList<>();
	}

	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#getType()
	 */
	@Override
	public String getType() {
		return SpdxConstantsCompatV2.CLASS_SPDX_NONE_ELEMENT;
	}
	
	@Override
	public Collection<Annotation> getAnnotations() throws InvalidSPDXAnalysisException {
		return annotations;
	}
	
	@Override
	public SpdxElement setAnnotations(Collection<Annotation> annotations) throws InvalidSPDXAnalysisException {
		throw new RuntimeException("Can not set annotations for NONE and NOASSERTION SPDX Elements");
	}
	
	@Override
	public boolean addAnnotation(Annotation annotation) throws InvalidSPDXAnalysisException {
		throw new RuntimeException("Can not add annotations for NONE and NOASSERTION SPDX Elements");
	}
	
	@Override
	public boolean removeAnnotation(Annotation annotation) throws InvalidSPDXAnalysisException {
		throw new RuntimeException("Can not remove annotations for NONE and NOASSERTION SPDX Elements");
	}
	
	@Override
	public Collection<Relationship> getRelationships() throws InvalidSPDXAnalysisException {
		return relationships;
	}
	
	@Override
	public SpdxElement setRelationships(Collection<Relationship> relationships) throws InvalidSPDXAnalysisException {
		throw new RuntimeException("Can not set relationships for NONE and NOASSERTION SPDX Elements");
	}
	
	@Override
	public boolean addRelationship(Relationship relationship) throws InvalidSPDXAnalysisException {
		throw new RuntimeException("Can not add relationships for NONE and NOASSERTION SPDX Elements");
	}
	
	@Override
	public boolean removeRelationship(Relationship relationship) throws InvalidSPDXAnalysisException {
		throw new RuntimeException("Can not remove relationships for NONE and NOASSERTION SPDX Elements");
	}
	
	@Override
	public void setComment(String comment) throws InvalidSPDXAnalysisException {
		throw new RuntimeException("Can not set comment for NONE and NOASSERTION SPDX Elements");
	}
	
	@Override
	public SpdxElement setName(String name) throws InvalidSPDXAnalysisException {
		throw new RuntimeException("Can not set name for NONE and NOASSERTION SPDX Elements");
	}
	
	@Override
	public boolean equals(Object comp) {
		return SimpleUriValue.isIndividualUriValueEquals(this, comp);
	}

	@Override
	public int hashCode() {
		return SimpleUriValue.getIndividualUriValueHash(this);
	}
}
