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

import java.io.File;
import java.util.Arrays;

import org.spdx.core.DefaultModelStore;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelRegistry;
import org.spdx.library.model.compat.v2.MockCopyManager;
import org.spdx.library.model.compat.v2.MockModelStore;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;
import org.spdx.library.model.v2.SpdxModelFactoryCompatV2;
import org.spdx.library.model.v2.SpdxModelInfoV2_X;
import org.spdx.library.model.v2.license.ExtractedLicenseInfo;

import junit.framework.TestCase;

/**
 * @author gary
 *
 */
public class ExtractedLicensingInfoTest extends TestCase {

	static final String TEST_RDF_FILE_PATH = "TestFiles"+File.separator+"SPDXRdfExample.rdf";
	static final String ID1 = SpdxConstantsCompatV2.NON_STD_LICENSE_ID_PRENUM + "1";
	static final String TEXT1 = "Text1";
	static final String TEXT2 = "Text2";
	static final String COMMENT1 = "Comment1";
	static final String COMMENT2 = "Comment2";
	static final String LICENSENAME1 = "license1";
	static final String LICENSENAME2 = "license2";
	static final String[] SOURCEURLS1 = new String[] {"url1", "url2"};
	static final String[] SOURCEURLS2 = new String[] {"url3", "url4", "url5"};
	
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

	/**
	 * Test method for {@link org.spdx.rdfparser.license.ExtractedLicenseInfo#equals(java.lang.Object)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	
	public void testEqualsObject() throws InvalidSPDXAnalysisException {
		ExtractedLicenseInfo lic1 = new ExtractedLicenseInfo(ID1, TEXT1);
		ExtractedLicenseInfo lic2 = new ExtractedLicenseInfo(ID1, TEXT2);
		if (!lic1.equals(lic2)) {
			fail("Should equal when ID's equal");
		}
		if (lic1.hashCode() != lic2.hashCode()) {
			fail("Hashcodes should equal");
		}
	}

	/**
	 * Test method for {@link org.spdx.rdfparser.license.ExtractedLicenseInfo#SPDXNonStandardLicense(java.lang.String, java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	
	public void testSPDXNonStandardLicenseStringString() throws InvalidSPDXAnalysisException {
		ExtractedLicenseInfo lic = new ExtractedLicenseInfo(ID1, TEXT1);
		assertEquals(ID1, lic.getLicenseId());
		assertEquals(TEXT1, lic.getExtractedText());
	}

	/**
	 * Test method for {@link org.spdx.rdfparser.license.ExtractedLicenseInfo#setExtractedText(java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	
	public void testSetText() throws InvalidSPDXAnalysisException {
		ExtractedLicenseInfo lic = new ExtractedLicenseInfo(ID1, TEXT1);
		lic.setComment(COMMENT1);
		ExtractedLicenseInfo lic2 = (ExtractedLicenseInfo)SpdxModelFactoryCompatV2.createModelObjectV2(DefaultModelStore.getDefaultModelStore(), DefaultModelStore.getDefaultDocumentUri(), 
				ID1, SpdxConstantsCompatV2.CLASS_SPDX_EXTRACTED_LICENSING_INFO, DefaultModelStore.getDefaultCopyManager());
		lic2.setExtractedText(TEXT2);
		assertEquals(ID1, lic2.getLicenseId());
		assertEquals(TEXT2, lic2.getExtractedText());
		assertEquals(COMMENT1, lic2.getComment());
	}

	/**
	 * Test method for {@link org.spdx.rdfparser.license.ExtractedLicenseInfo#setComment(java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	
	public void testSetComment() throws InvalidSPDXAnalysisException {
		ExtractedLicenseInfo lic = new ExtractedLicenseInfo(ID1, TEXT1);
		lic.setComment(COMMENT1);
		ExtractedLicenseInfo lic2 = (ExtractedLicenseInfo)SpdxModelFactoryCompatV2.createModelObjectV2(DefaultModelStore.getDefaultModelStore(), DefaultModelStore.getDefaultDocumentUri(), 
				ID1, SpdxConstantsCompatV2.CLASS_SPDX_EXTRACTED_LICENSING_INFO, DefaultModelStore.getDefaultCopyManager());
		lic2.setComment(COMMENT2);
		assertEquals(ID1, lic2.getLicenseId());
		assertEquals(TEXT1, lic2.getExtractedText());
		assertEquals(COMMENT2, lic2.getComment());
	}
	
	public void testSetLicenseName() throws InvalidSPDXAnalysisException {
		ExtractedLicenseInfo lic = new ExtractedLicenseInfo(ID1, TEXT1);
		lic.setName(LICENSENAME1);
		ExtractedLicenseInfo lic2 = (ExtractedLicenseInfo)SpdxModelFactoryCompatV2.createModelObjectV2(DefaultModelStore.getDefaultModelStore(), 
				DefaultModelStore.getDefaultDocumentUri(), ID1, SpdxConstantsCompatV2.CLASS_SPDX_EXTRACTED_LICENSING_INFO, DefaultModelStore.getDefaultCopyManager());
		lic2.setName(LICENSENAME2);
		assertEquals(LICENSENAME2, lic2.getName());
	}
	
	
	public void testSetSourceUrls() throws InvalidSPDXAnalysisException {
		ExtractedLicenseInfo lic = new ExtractedLicenseInfo(ID1, TEXT1);
		lic.setSeeAlso(Arrays.asList(SOURCEURLS1));
		ExtractedLicenseInfo lic2 = (ExtractedLicenseInfo)SpdxModelFactoryCompatV2.createModelObjectV2(DefaultModelStore.getDefaultModelStore(), DefaultModelStore.getDefaultDocumentUri(), 
				ID1, SpdxConstantsCompatV2.CLASS_SPDX_EXTRACTED_LICENSING_INFO, DefaultModelStore.getDefaultCopyManager());
		lic2.setSeeAlso(Arrays.asList(SOURCEURLS2));
		if (!compareArrayContent(SOURCEURLS2, (String[])lic2.getSeeAlso().toArray(new String[lic2.getSeeAlso().size()]))) {
			fail("Source URLS not the same");
		}
	}

	/**
	 * @param strings1
	 * @param strings2
	 * @return true if both arrays contain the same content independent of order
	 */
	private boolean compareArrayContent(String[] strings1,
			String[] strings2) {
		if (strings1.length != strings2.length) {
			return false;
		}
		for (int i = 0; i < strings1.length; i++) {
			boolean found = false;
			for (int j = 0; j < strings2.length; j++) {
				if (strings1[i].equals(strings2[j])) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
	
	public void testEquivalent() throws InvalidSPDXAnalysisException {
		ExtractedLicenseInfo lic = new ExtractedLicenseInfo(ID1, TEXT1);
		lic.setName(LICENSENAME1);
		lic.setSeeAlso(Arrays.asList(SOURCEURLS1));
		lic.setComment(COMMENT1);
		assertTrue(lic.equivalent(lic));
		String docUri2 = "https://second.doc.uri";
		ExtractedLicenseInfo lic2 = new ExtractedLicenseInfo(DefaultModelStore.getDefaultModelStore(), docUri2, ID1, DefaultModelStore.getDefaultCopyManager(), true);
		lic2.setExtractedText(TEXT1+"    ");
		lic2.setName(LICENSENAME2);
		lic2.setSeeAlso(Arrays.asList(SOURCEURLS2));
		lic2.setComment(COMMENT2);
		assertTrue(lic.equivalent(lic2));
		lic2.setExtractedText(TEXT2);
		assertFalse(lic.equivalent(lic2));
	}
}
