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
package org.spdx.library.model.compat.v2.license;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.spdx.core.DefaultModelStore;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelRegistry;
import org.spdx.library.model.compat.v2.MockCopyManager;
import org.spdx.library.model.compat.v2.MockModelStore;
import org.spdx.library.model.v2.SpdxModelInfoV2_X;
import org.spdx.library.model.v2.license.LicenseException;
import org.spdx.library.model.v2.license.ListedLicenseException;

import junit.framework.TestCase;

/**
 * @author Gary O'Neall
 *
 */
public class LicenseExceptionTest extends TestCase {
	
	static final String EXCEPTION_ID1 = "id1";
	static final String EXCEPTION_NAME1 = "name1";
	static final String EXCEPTION_TEXT1 = "exception text one";
	static final String EXCEPTION_TEMPLATE1 = "exception text one <<beginOptional>>extra<<endOptional>>";
	static final List<String> EXCEPTION_SEEALSO1 = Arrays.asList(new String[] {"http://url1", "http://url2"});
	static final String EXCEPTION_COMMENT1 = "comment1";
	static final String EXCEPTION_EXAMPLE1 = "example1";
	static final String EXCEPTION_ID2 = "id2";
	static final String EXCEPTION_NAME2 = "name2";
	static final String EXCEPTION_TEXT2 = "exception text two";
	static final String EXCEPTION_TEMPLATE2 = "exception text <<beginOptional>>two<<endOptional>> again <<beginOptional>>extra<<endOptional>>";
	static final List<String> EXCEPTION_SEEALSO2 =  Arrays.asList(new String[] {"http://url3"});
	static final String EXCEPTION_COMMENT2 = "comment2";
	static final String EXCEPTION_EXAMPLE2 = "example2";
	static final String TEST_NAMESPACE = "http://spdx.org/test/namespace";
	private static final String EXCEPTION_HTML_TEXT1 = "exception text one \n<div class=\"optional-license-text\">\nextra</div>\n";
	private static final String EXCEPTION_HTML_TEXT2 = "<strong>exception text one extra</strong>";

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ModelRegistry.getModelRegistry().registerModel(new SpdxModelInfoV2_X());
		DefaultModelStore.initialize(new MockModelStore(), "http://defaultdocument", new MockCopyManager());
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testLicenseException() throws InvalidSPDXAnalysisException {
		LicenseException le = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le.getName());
		assertEquals(EXCEPTION_TEXT1, le.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le.getComment());
	}
	
	public void testEquals() throws InvalidSPDXAnalysisException {
		LicenseException le = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_TEMPLATE1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		LicenseException le2 = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME2, EXCEPTION_TEXT2, EXCEPTION_TEMPLATE2, EXCEPTION_SEEALSO2,
				EXCEPTION_COMMENT2);
		LicenseException le3 = new ListedLicenseException(EXCEPTION_ID2,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_TEMPLATE1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		assertTrue(le.equals(le2));
		assertFalse(le.equals(le3));
	}

	
	public void testSetComment() throws InvalidSPDXAnalysisException {
		LicenseException le = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le.getName());
		assertEquals(EXCEPTION_TEXT1, le.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le.getComment());
		le.setComment(EXCEPTION_COMMENT2);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le.getName());
		assertEquals(EXCEPTION_TEXT1, le.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT2, le.getComment());
		LicenseException le2 = new ListedLicenseException(DefaultModelStore.getDefaultModelStore(), DefaultModelStore.getDefaultDocumentUri(),
				EXCEPTION_ID1, DefaultModelStore.getDefaultCopyManager(), false);
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le2.getName());
		assertEquals(EXCEPTION_TEXT1, le2.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT2, le2.getComment());
		le2.setComment(EXCEPTION_COMMENT1);
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le2.getName());
		assertEquals(EXCEPTION_TEXT1, le2.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le2.getComment());
	}
	
	
	public void testSetDeprecated() throws InvalidSPDXAnalysisException {
		LicenseException le = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		assertFalse(le.isDeprecated());
		LicenseException le2 = new ListedLicenseException(DefaultModelStore.getDefaultModelStore(), 
				DefaultModelStore.getDefaultDocumentUri(), EXCEPTION_ID1, DefaultModelStore.getDefaultCopyManager(), false);
		assertFalse(le2.isDeprecated());
		le2.setDeprecated(true);
		assertTrue(le2.isDeprecated());
		LicenseException le3 = new ListedLicenseException(DefaultModelStore.getDefaultModelStore(), 
				DefaultModelStore.getDefaultDocumentUri(), EXCEPTION_ID1, DefaultModelStore.getDefaultCopyManager(), false);
		assertTrue(le3.isDeprecated());
	}
	
	public void testSetLicenseExceptionText() throws InvalidSPDXAnalysisException {
		LicenseException le = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le.getName());
		assertEquals(EXCEPTION_TEXT1, le.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le.getComment());
		le.setLicenseExceptionText(EXCEPTION_TEXT2);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le.getName());
		assertEquals(EXCEPTION_TEXT2, le.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le.getComment());
		LicenseException le2 = new ListedLicenseException(DefaultModelStore.getDefaultModelStore(), 
				DefaultModelStore.getDefaultDocumentUri(), EXCEPTION_ID1, DefaultModelStore.getDefaultCopyManager(), false);
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le2.getName());
		assertEquals(EXCEPTION_TEXT2, le2.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le2.getComment());
		le2.setLicenseExceptionText(EXCEPTION_TEXT1);
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le2.getName());
		assertEquals(EXCEPTION_TEXT1, le2.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le2.getComment());
	}

	
	public void testSetLicenseExceptionTemplate() throws InvalidSPDXAnalysisException {
		LicenseException le = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_TEMPLATE1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le.getName());
		assertEquals(EXCEPTION_TEXT1, le.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le.getComment());
		assertEquals(EXCEPTION_TEMPLATE1, le.getLicenseExceptionTemplate());
		le.setLicenseExceptionTemplate(EXCEPTION_TEMPLATE2);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le.getName());
		assertEquals(EXCEPTION_TEMPLATE2, le.getLicenseExceptionTemplate());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le.getComment());
		LicenseException le2 = new ListedLicenseException(DefaultModelStore.getDefaultModelStore(), DefaultModelStore.getDefaultDocumentUri(), 
				EXCEPTION_ID1, DefaultModelStore.getDefaultCopyManager(), false);
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le2.getName());
		assertEquals(EXCEPTION_TEMPLATE2, le2.getLicenseExceptionTemplate());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le2.getComment());
		le2.setLicenseExceptionTemplate(EXCEPTION_TEMPLATE1);
		assertEquals(EXCEPTION_TEMPLATE1, le2.getLicenseExceptionTemplate());
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le2.getName());
		assertEquals(EXCEPTION_TEMPLATE1, le2.getLicenseExceptionTemplate());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le2.getComment());
	}
	
	
	public void testSetSeeAlso() throws InvalidSPDXAnalysisException {
		LicenseException le = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le.getName());
		assertEquals(EXCEPTION_TEXT1, le.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le.getComment());
		le.setSeeAlso(EXCEPTION_SEEALSO2);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le.getName());
		assertEquals(EXCEPTION_TEXT1, le.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO2, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le.getComment());
		LicenseException le2 = new ListedLicenseException(DefaultModelStore.getDefaultModelStore(), 
				DefaultModelStore.getDefaultDocumentUri(), EXCEPTION_ID1, DefaultModelStore.getDefaultCopyManager(), false);
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le2.getName());
		assertEquals(EXCEPTION_TEXT1, le2.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO2, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le2.getComment());
		le2.setSeeAlso(EXCEPTION_SEEALSO1);
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le2.getName());
		assertEquals(EXCEPTION_TEXT1, le2.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le2.getComment());
	}

	
	public void testSetName() throws InvalidSPDXAnalysisException {
		LicenseException le = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le.getName());
		assertEquals(EXCEPTION_TEXT1, le.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le.getComment());
		le.setName(EXCEPTION_NAME2);
		assertEquals(EXCEPTION_ID1, le.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME2, le.getName());
		assertEquals(EXCEPTION_TEXT1, le.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le.getComment());
		LicenseException le2 = new ListedLicenseException(DefaultModelStore.getDefaultModelStore(), 
				DefaultModelStore.getDefaultDocumentUri(), EXCEPTION_ID1, DefaultModelStore.getDefaultCopyManager(), false);
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME2, le2.getName());
		assertEquals(EXCEPTION_TEXT1, le2.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le2.getComment());
		le2.setName(EXCEPTION_NAME1);
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le2.getName());
		assertEquals(EXCEPTION_TEXT1, le2.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le2.getComment());
	}

	
	public void testHashCode() throws InvalidSPDXAnalysisException {
		LicenseException le = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_TEMPLATE1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		LicenseException le2 = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME2, EXCEPTION_TEXT2, EXCEPTION_TEMPLATE2, EXCEPTION_SEEALSO2,
				EXCEPTION_COMMENT2);
		LicenseException le3 = new ListedLicenseException(EXCEPTION_ID2,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_TEMPLATE1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		assertEquals(le.hashCode(), le2.hashCode());
		assertFalse(le.hashCode() == le3.hashCode());
	}

	
	public void testVerify() throws InvalidSPDXAnalysisException {
		LicenseException le = new ListedLicenseException(EXCEPTION_ID1,
				EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_TEMPLATE1, EXCEPTION_SEEALSO1,
				EXCEPTION_COMMENT1);
		assertEquals(0,le.verify().size());
		LicenseException le2 = new ListedLicenseException(DefaultModelStore.getDefaultModelStore(), 
				DefaultModelStore.getDefaultDocumentUri(), EXCEPTION_ID1, DefaultModelStore.getDefaultCopyManager(), false);
		assertEquals(EXCEPTION_ID1, le2.getLicenseExceptionId());
		assertEquals(EXCEPTION_NAME1, le2.getName());
		assertEquals(EXCEPTION_TEXT1, le2.getLicenseExceptionText());
		assertStringsCollectionsEquals(EXCEPTION_SEEALSO1, le2.getSeeAlso());
		assertEquals(EXCEPTION_COMMENT1, le2.getComment());
		assertEquals(0,le2.verify().size());
		le2.setLicenseExceptionText("  ");
		assertEquals(1, le2.verify().size());
	}

	/**
	 * @param s1
	 * @param s2
	 */
	private void assertStringsCollectionsEquals(Collection<String> s1,
			Collection<String> s2) {
		if (s1 == null) {
			assertTrue(s2 == null);
		}
		assertTrue(s2 != null);
		assertEquals(s1.size(), s2.size());
		for (String s:s1) {
			assertTrue(s2.contains(s));
		}
	}
	 
	 public void testListedLicenseException() throws InvalidSPDXAnalysisException {
			ListedLicenseException le = new ListedLicenseException(EXCEPTION_ID1,
					EXCEPTION_NAME1, EXCEPTION_TEXT1, EXCEPTION_TEMPLATE1, EXCEPTION_SEEALSO1,
					EXCEPTION_COMMENT1);
			// No HTML set yet
			String result = le.getExceptionTextHtml();
			assertEquals(EXCEPTION_HTML_TEXT1, result);
			le.setExceptionTextHtml(EXCEPTION_HTML_TEXT2);
			result = le.getExceptionTextHtml();
			assertEquals(EXCEPTION_HTML_TEXT2, result);
	 }

}
