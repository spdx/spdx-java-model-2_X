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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.spdx.core.DefaultModelStore;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelRegistry;
import org.spdx.library.model.compat.v2.MockCopyManager;
import org.spdx.library.model.compat.v2.MockModelStore;
import org.spdx.library.model.v2.SpdxModelInfoV2_X;
import org.spdx.library.model.v2.license.CrossRef;
import org.spdx.library.model.v2.license.SpdxListedLicense;
import org.spdx.licenseTemplate.InvalidLicenseTemplateException;

import junit.framework.TestCase;

/**
 * @author gary
 *
 */
public class SpdxListedLicenseTest extends TestCase {

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

	public void testCreate() throws InvalidSPDXAnalysisException, InvalidLicenseTemplateException {
		String name = "name";
		String id = "AFL-3.0";
		String text = "text";
		Collection<String> sourceUrls = new ArrayList<String>(Arrays.asList(new String[] {"source url1", "source url2"}));
		String notes = "notes";
		String standardLicenseHeader = "Standard license header";
		String template = "template";
		String licenseHtml = "<html>html</html>";
		SpdxListedLicense stdl = new SpdxListedLicense(name, id, text,
				sourceUrls, notes, standardLicenseHeader, template, true, true, licenseHtml, false, null);
		SpdxListedLicense compLic = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertEquals(id, compLic.getLicenseId());
		assertEquals(text, compLic.getLicenseText());
		List<String> verify = stdl.verify();
		assertEquals(0, verify.size());
		verify = compLic.verify();
		assertEquals(0, verify.size());
		assertEquals(name, compLic.getName());
		assertEquals(sourceUrls.size(), compLic.getSeeAlso().size());
		
		assertEquals(notes, compLic.getComment());
		assertEquals(standardLicenseHeader, compLic.getStandardLicenseHeader());
		assertEquals(template, compLic.getStandardLicenseTemplate());
		assertTrue(compLic.isFsfLibre());
		assertTrue(compLic.isOsiApproved());
		assertFalse(compLic.isDeprecated());
	}
	

	public void testSetComment() throws InvalidSPDXAnalysisException {

		String name = "name";
		String id = "AFL-3.0";
		String text = "text";
		Collection<String> sourceUrls = new ArrayList<String>(Arrays.asList(new String[] {"source url2", "source url3"}));
		String comments = "comments1";
		String comments2 = "comments2";
		String standardLicenseHeader = "Standard license header";
		String template = "template";
		SpdxListedLicense stdl = new SpdxListedLicense(name, id, text,
				sourceUrls, comments, standardLicenseHeader, template, true, false, null, false, null);
		SpdxListedLicense compLic = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertEquals(comments, compLic.getComment());
		
		compLic.setComment(comments2);
		assertEquals(comments2, compLic.getComment());
		SpdxListedLicense compLic2 = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertEquals(comments2, compLic2.getComment());

		List<String> verify = stdl.verify();
		assertEquals(0, verify.size());
		verify = compLic.verify();
		assertEquals(0, verify.size());
	}
	

	public void testSetFsfLibre() throws InvalidSPDXAnalysisException {

		String name = "name";
		String id = "AFL-3.0";
		String text = "text";
		Collection<String> sourceUrls = new ArrayList<String>(Arrays.asList(new String[] {"source url1", "source url2"}));
		String notes = "notes";
		String standardLicenseHeader = "Standard license header";
		String template = "template";
		String licenseHtml = "<html>html</html>";
		String deprecatedVersion = "3.2";
		SpdxListedLicense stdl = new SpdxListedLicense(name, id, text,
				sourceUrls, notes, standardLicenseHeader, template, false, false, licenseHtml, false, null);
		assertFalse(stdl.isFsfLibre());
		stdl.setFsfLibre(true);
		assertTrue(stdl.isFsfLibre());
		SpdxListedLicense compLic = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertTrue(stdl.isFsfLibre());
		compLic.setFsfLibre(false);
		assertFalse(compLic.isFsfLibre());
		SpdxListedLicense compLic2 = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertFalse(compLic2.isFsfLibre());
		List<String> verify = stdl.verify();
		assertEquals(0, verify.size());
		verify = compLic.verify();
		assertEquals(0, verify.size());
		
		// Test for null value
		SpdxListedLicense stdl2 = new SpdxListedLicense(name, id, text,
				sourceUrls, notes, standardLicenseHeader, template, false, null, licenseHtml, true, deprecatedVersion);
		assertTrue(stdl2.getFsfLibre() == null);
		assertFalse(stdl2.isFsfLibre());
		assertFalse(stdl2.isNotFsfLibre());
		SpdxListedLicense compLic3 = new SpdxListedLicense(stdl2.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertTrue(compLic3.getFsfLibre() == null);
		assertFalse(compLic3.isFsfLibre());
		assertFalse(compLic3.isNotFsfLibre());
		compLic3.setFsfLibre(false);
		assertFalse(compLic3.getFsfLibre() == null);
		assertFalse(compLic3.isFsfLibre());
		assertTrue(compLic3.isNotFsfLibre());
		SpdxListedLicense compLic4 = new SpdxListedLicense(stdl2.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertFalse(compLic4.getFsfLibre() == null);
		assertFalse(compLic4.isFsfLibre());		
		assertTrue(compLic4.isNotFsfLibre());
	}
	

	public void testSetDeprecated() throws InvalidSPDXAnalysisException {

		String name = "name";
		String id = "AFL-3.0";
		String text = "text";
		Collection<String> sourceUrls = new ArrayList<String>(Arrays.asList(new String[] {"source url2", "source url3"}));
		String comments = "comments1";
		String standardLicenseHeader = "Standard license header";
		String template = "template";
		SpdxListedLicense stdl = new SpdxListedLicense(name, id, text,
				sourceUrls, comments, standardLicenseHeader, template, true,null, null, false, null);
		stdl.setDeprecated(true);
		SpdxListedLicense compLic = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertEquals(true, compLic.isDeprecated());
		
		compLic.setDeprecated(false);
		assertEquals(false, compLic.isDeprecated());
		SpdxListedLicense compLic2 = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertEquals(false, compLic2.isDeprecated());
		List<String> verify = stdl.verify();
		assertEquals(0, verify.size());
		verify = compLic.verify();
		assertEquals(0, verify.size());
	}


	public void testSetText() throws InvalidSPDXAnalysisException {

		String name = "name";
		String id = "AFL-3.0";
		String text = "text";
		Collection<String> sourceUrls = new ArrayList<String>(Arrays.asList(new String[] {"source url2", "source url3"}));
		String notes = "notes";
		String standardLicenseHeader = "Standard license header";
		String template = "template";
		SpdxListedLicense stdl = new SpdxListedLicense(name, id, text,
				sourceUrls, notes, standardLicenseHeader, template, true,null, null, false, null);
		SpdxListedLicense compLic = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertEquals(id, compLic.getLicenseId());
		assertEquals(text, compLic.getLicenseText());

		String newText = "new Text";
		compLic.setLicenseText(newText);
		assertEquals(newText, compLic.getLicenseText());
		SpdxListedLicense compLic2 = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertEquals(newText, compLic2.getLicenseText());
		List<String> verify = stdl.verify();
		assertEquals(0, verify.size());	
		verify = compLic.verify();
		assertEquals(0, verify.size());	
	}

	public void testSetHeaderTemplate() throws InvalidSPDXAnalysisException {

		String name = "name";
		String id = "AFL-3.0";
		String text = "text";
		Collection<String> sourceUrls = new ArrayList<String>(Arrays.asList(new String[] {"source url2", "source url3"}));
		String notes = "notes";
		String standardLicenseHeader = "Standard license header";
		String standardLicenseHeaderTemplate = "Standard license<<beginOptional>>optional<<endOptional>> header";
		String template = "template";
		SpdxListedLicense stdl = new SpdxListedLicense(name, id, text,
				sourceUrls, notes, standardLicenseHeader, template, true,null, null, false, null);
		stdl.setStandardLicenseHeaderTemplate(standardLicenseHeaderTemplate);
		assertEquals(standardLicenseHeaderTemplate, stdl.getStandardLicenseHeaderTemplate());
		SpdxListedLicense compLic = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertEquals(standardLicenseHeaderTemplate, compLic.getStandardLicenseHeaderTemplate());
		
		String newHeaderTemplate = "New standard license template";
		compLic.setStandardLicenseHeaderTemplate(newHeaderTemplate);
		assertEquals(newHeaderTemplate, compLic.getStandardLicenseHeaderTemplate());
		SpdxListedLicense compLic2 = new SpdxListedLicense(stdl.getModelStore(), stdl.getDocumentUri(), id, stdl.getCopyManager(), false);
		assertEquals(newHeaderTemplate, compLic2.getStandardLicenseHeaderTemplate());
		List<String> verify = stdl.verify();
		assertEquals(0, verify.size());
		verify = compLic.verify();
		assertEquals(0, verify.size());
	}
	

	public void testSetHeaderTemplateHtml() throws InvalidSPDXAnalysisException, InvalidLicenseTemplateException {
		String name = "name";
		String id = "AFL-3.0";
		String text = "text";
		Collection<String> sourceUrls = new ArrayList<String>(Arrays.asList(new String[] {"source url2", "source url3"}));
		String notes = "notes";
		String standardLicenseHeader = "Standard license header";
		String standardLicenseHeaderTemplate = "Standard license<<beginOptional>>optional<<endOptional>> header";
		String template = "template";
		String standardLicenseHeaderHtml = "<h1>licenseHeader</h1>";
		String textHtml = "<h1>text</h1>";
		SpdxListedLicense stdl = new SpdxListedLicense(name, id, text,
				sourceUrls, notes, standardLicenseHeader, template, false, 
				true, textHtml, false, null);
		stdl.setStandardLicenseHeaderTemplate(standardLicenseHeaderTemplate);
		stdl.setLicenseHeaderHtml(standardLicenseHeaderHtml);
		assertEquals(textHtml, stdl.getLicenseTextHtml());
		assertEquals(standardLicenseHeaderHtml, stdl.getLicenseHeaderHtml());
		String newStandardLicenseHeaderHtml = "<h2>licenseHeader2</h2>";
		String newTextHtml = "<h2>text2</h2>";
		stdl.setLicenseTextHtml(newTextHtml);
		stdl.setLicenseHeaderHtml(newStandardLicenseHeaderHtml);
		assertEquals(newTextHtml, stdl.getLicenseTextHtml());
		assertEquals(newStandardLicenseHeaderHtml, stdl.getLicenseHeaderHtml());
	}
	
	public void testCrossRef() throws InvalidSPDXAnalysisException {
		String name = "name";
		String id = "AFL-3.0";
		String text = "text";
		String url1 = "http://url1";
		String url2 = "http://url2";
		Boolean isLive1 = true;
		Boolean isLive2 = false;
		Boolean isWayback1 = true;
		Boolean isWayback2 = false;
		Boolean isValid1 = true;
		Boolean isValid2 = false;
		String match1 = "true";
		String match2 = "false";
		String date1 = "today";
		String date2 = "tomorrow";
		Integer order1 = 1;
		Integer order2 = 2;
		Collection<String> sourceUrls = new ArrayList<String>(Arrays.asList(new String[] {url1, url2}));
		String notes = "notes";
		String standardLicenseHeader = "Standard license header";
		String template = "template";
		String licenseHtml = "<html>html</html>";
		SpdxListedLicense stdl = new SpdxListedLicense(name, id, text,
				sourceUrls, notes, standardLicenseHeader, template, true, true, licenseHtml, false, null);
		assertEquals(0, stdl.getCrossRef().size());
		CrossRef testCrossRef1 = stdl.createCrossRef(url1)
				.setLive(isLive1)
				.setMatch(match1)
				.setOrder(order1)
				.setTimestamp(date1)
				.setValid(isValid1)
				.setWayBackLink(isWayback1)
				.build();
		CrossRef testCrossRef2 = stdl.createCrossRef(url2)
				.setLive(isLive2)
				.setMatch(match2)
				.setOrder(order2)
				.setTimestamp(date2)
				.setValid(isValid2)
				.setWayBackLink(isWayback2)
				.build();
		stdl.getCrossRef().add(testCrossRef1);
		assertEquals(1, stdl.getCrossRef().size());
		for (CrossRef ref:stdl.getCrossRef()) {
			assertTrue(testCrossRef1.equivalent(ref));
			assertEquals(url1, testCrossRef1.getUrl().get());
		}
		stdl.getCrossRef().add(testCrossRef2);
		assertEquals(2, stdl.getCrossRef().size());
		for (CrossRef ref:stdl.getCrossRef()) {
			if (ref.getUrl().get().equals(url1)) {
				assertTrue(testCrossRef1.equivalent(ref));
			} else {
				assertTrue(testCrossRef2.equivalent(ref));
			}
		}
	}
}
