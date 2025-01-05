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
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import org.spdx.core.IModelCopyManager;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.SpdxInvalidTypeException;
import org.spdx.library.model.v2.enumerations.AnnotationType;
import org.spdx.storage.IModelStore;

/**
 * An Annotation is a comment on an SpdxItem by an agent.
 *
 * @author Gary O'Neall
 */
public class Annotation extends ModelObjectV2 implements Comparable<Annotation> {

	/**
	 * @throws InvalidSPDXAnalysisException
	 */
	public Annotation() throws InvalidSPDXAnalysisException {
		super();
	}

	/**
	 * @param id SPDX ID of the annotation
	 * @throws InvalidSPDXAnalysisException
	 */
	public Annotation(String id) throws InvalidSPDXAnalysisException {
		super(id);
	}

	/**
	 * @param modelStore container which includes the annotation
	 * @param documentUri URI for the SPDX document containing the annotation
	 * @param id identifier for the license
	 * @param copyManager if non-null, allows for copying of any properties set which use other model stores or document URI's
	 * @param create if true, create the license if it does not exist
	 * @throws InvalidSPDXAnalysisException
	 */
	public Annotation(IModelStore modelStore, String documentUri, String id, 
			@Nullable IModelCopyManager copyManager, boolean create)
			throws InvalidSPDXAnalysisException {
		super(modelStore, documentUri, id, copyManager, create);
	}

	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#getType()
	 */
	@Override
	public String getType() {
		return SpdxConstantsCompatV2.CLASS_ANNOTATION;
	}

	/**
	 * @return annotation type
	 * @throws InvalidSPDXAnalysisException
	 */
	public AnnotationType getAnnotationType() throws InvalidSPDXAnalysisException {
		Optional<Enum<?>> retval = getEnumPropertyValue(SpdxConstantsCompatV2.PROP_ANNOTATION_TYPE);
		if (retval.isPresent() && !(retval.get() instanceof AnnotationType)) {
			throw new SpdxInvalidTypeException("Invalid enum type for "+retval.get().toString());
		}
		if (retval.isPresent()) {
			if (!(retval.get() instanceof AnnotationType)) {
				throw new SpdxInvalidTypeException("Invalid enum type for "+retval.get().toString());
			}
			return (AnnotationType)retval.get();
		} else {
			logger.warn("Missing required annotation type, returning type missing for "+getId());
			return AnnotationType.MISSING;
		}
	}
	
	/**
	 * @param type
	 * @return this to chain setters
	 * @throws InvalidSPDXAnalysisException
	 */
	public Annotation setAnnotationType(AnnotationType type) throws InvalidSPDXAnalysisException {
		if (strict) {
			if (type == null) {
				throw new InvalidSPDXAnalysisException("Annotation type is required - null value for type is not accepted");
			}
		}
		if (AnnotationType.MISSING.equals(type)) {
			throw new InvalidSPDXAnalysisException("Can not set value to MISSING for annotation type.  This is reserved for when the value is not present in the store.");
		}
		setPropertyValue(SpdxConstantsCompatV2.PROP_ANNOTATION_TYPE, type);
		return this;
	}
	
	/**
	 * @return the annotator
	 * @throws InvalidSPDXAnalysisException 
	 */
	public String getAnnotator() throws InvalidSPDXAnalysisException {
		Optional<String> retval = getStringPropertyValue(SpdxConstantsCompatV2.PROP_ANNOTATOR);
		if (retval.isPresent()) {
			return retval.get();
		} else {
			logger.warn("Missing required annotator for "+getId());
			return "";
		}
	}
	
	/**
	 * @param annotator
	 * @return this to chain setters
	 * @throws InvalidSPDXAnalysisException
	 */
	public Annotation setAnnotator(String annotator) throws InvalidSPDXAnalysisException {
		if (strict) {
			if (annotator == null || annotator.isEmpty()) {
				throw new InvalidSPDXAnalysisException("Annotator is required - can not be null or empty");
			}
			String verify = SpdxVerificationHelper.verifyAnnotator(annotator);
			if (verify != null && !verify.isEmpty()) {
				throw new InvalidSPDXAnalysisException(verify);
			}
		}
		setPropertyValue(SpdxConstantsCompatV2.PROP_ANNOTATOR, annotator);
		return this;
	}
	
	/**
	 * @return the comment
	 */
	public String getComment() throws InvalidSPDXAnalysisException {
		Optional<String> retval = getStringPropertyValue(SpdxConstantsCompatV2.RDFS_PROP_COMMENT);
		if (retval.isPresent()) {
			return retval.get();
		} else {
			logger.warn("Missing required comment for "+getId());
			return "";
		}
	}
	
	/**
	 * Set the comment
	 * @param comment
	 * @return this to chain setters
	 * @throws InvalidSPDXAnalysisException
	 */
	public Annotation setComment(String comment) throws InvalidSPDXAnalysisException {
		if (strict) {
			if (comment == null || comment.isEmpty()) {
				throw new InvalidSPDXAnalysisException("Comment is required - can not be null or empty");
			}
		}
		setPropertyValue(SpdxConstantsCompatV2.RDFS_PROP_COMMENT, comment);
		return this;
	}
	
	/**
	 * @return the date
	 */
	public String getAnnotationDate() throws InvalidSPDXAnalysisException {
		Optional<String> retval = getStringPropertyValue(SpdxConstantsCompatV2.PROP_ANNOTATION_DATE);
		if (retval.isPresent()) {
			return retval.get();
		} else {
			logger.warn("Missing required annotation date for "+getId());
			return "";
		}
	}
	
	/**
	 * Set the annotation date
	 * @param date
	 * @return this to chain setters
	 * @throws InvalidSPDXAnalysisException
	 */
	public Annotation setAnnotationDate(String date) throws InvalidSPDXAnalysisException {
		if (strict) {
			if (date == null || date.isEmpty()) {
				throw new InvalidSPDXAnalysisException("Date is required - can not be null or empty");
			}
			String dateVerify = SpdxVerificationHelper.verifyDate(date);
			if (dateVerify != null && !dateVerify.isEmpty()) {
				throw new InvalidSPDXAnalysisException(dateVerify);
			}
		}
		setPropertyValue(SpdxConstantsCompatV2.PROP_ANNOTATION_DATE, date);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.ModelObject#_verify(java.util.List)
	 */
	@Override
	protected List<String> _verify(Set<String> verifiedIds, String specVersion) {
		List<String> retval = new ArrayList<String>();
		try {
			if (AnnotationType.MISSING.equals(getAnnotationType())) {
				retval.add("Missing annotationtype for Annotation");
			}
		} catch (InvalidSPDXAnalysisException e) {
			retval.add("Error getting annotationtype for Annotation: "+e.getMessage());
		}
		try {
			String annotator = getAnnotator();
			String v = SpdxVerificationHelper.verifyAnnotator(annotator);
			if (v != null && !v.isEmpty()) {
				retval.add(v + ":" + annotator);
			}
		} catch (InvalidSPDXAnalysisException e) {
			retval.add("Error getting annotator for Annotation: "+e.getMessage());
		}
		try {
			if (getComment().isEmpty()) {
				retval.add("Missing required comment for Annotation");
			}
		} catch (InvalidSPDXAnalysisException e) {
			retval.add("Error getting comment for Annotation: "+e.getMessage());
		}
		try {
			String date = getAnnotationDate();
			if (date.isEmpty()) {
				retval.add("Missing required date for Annotation");
			} else {
				String dateVerify = SpdxVerificationHelper.verifyDate(date);
				if (dateVerify != null && !dateVerify.isEmpty()) {
					retval.add(dateVerify);
				}
			}
		} catch (InvalidSPDXAnalysisException e) {
			retval.add("Error getting date for Annotation: "+e.getMessage());
		}
		
		return retval;
	}

	@Override
	public int compareTo(Annotation o) {
		try {
			if (o == null) {
				return 1;
			}
			if (o.getAnnotationDate() == null) {
				if (this.getAnnotationDate() != null) {
					return 1;
				}
			}
			if (this.getAnnotationDate() == null) {
				return -1;
			}
			int retval = this.getAnnotationDate().compareTo(o.getAnnotationDate());
			if (retval != 0) {
				return retval;
			}
			if (o.getAnnotator() == null) {
				if (this.getAnnotator() != null) {
					return 1;
				}
			}
			if (this.getAnnotator() == null) {
				return -1;
			}
			retval = this.getAnnotator().compareToIgnoreCase(o.getAnnotator());
			if (retval != 0) {
				return retval;
			}
			if (o.getAnnotationType() == null) {
				if (this.getAnnotationType() != null) {
					return 1;
				}
			}
			if (this.getAnnotationType() == null) {
				return -1;
			}
			return this.getAnnotationType().compareTo(o.getAnnotationType());
		} catch(InvalidSPDXAnalysisException ex) {
			logger.warn("Error when comparing",ex);
			return -1;
		}
	}
}
