package org.spdx.library.model.compat.v2.license;

import org.spdx.core.DefaultModelStore;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelRegistry;
import org.spdx.library.model.compat.v2.MockCopyManager;
import org.spdx.library.model.compat.v2.MockModelStore;
import org.spdx.library.model.v2.SpdxModelInfoV2_X;
import org.spdx.library.model.v2.license.ExtractedLicenseInfo;
import org.spdx.library.model.v2.license.LicenseException;
import org.spdx.library.model.v2.license.ListedLicenseException;
import org.spdx.library.model.v2.license.SimpleLicensingInfo;
import org.spdx.library.model.v2.license.WithExceptionOperator;

import junit.framework.TestCase;

public class WithExceptionOperatorTest extends TestCase {
	
	static final String LICENSE_ID1 = "LicenseRef-1";
	static final String LICENSE_TEXT1 = "licenseText";
	static final String EXCEPTION_ID1 = "Exception-1";
	static final String EXCEPTION_NAME1 = "ExceptionName";
	static final String EXCEPTION_TEXT1 = "ExceptionText";
	static final String LICENSE_ID2 = "LicenseRef-2";
	static final String LICENSE_TEXT2 = "Second licenseText";
	static final String EXCEPTION_ID2 = "Exception-2";
	static final String EXCEPTION_NAME2 = "Second ExceptionName";
	static final String EXCEPTION_TEXT2 = "Second ExceptionText";

	private SimpleLicensingInfo license1;
	private SimpleLicensingInfo license2;
	private LicenseException exception1;
	private LicenseException exception2;

	protected void setUp() throws Exception {
		super.setUp();
		ModelRegistry.getModelRegistry().registerModel(new SpdxModelInfoV2_X());
		DefaultModelStore.initialize(new MockModelStore(), "http://defaultdocument", new MockCopyManager());
		license1 = new ExtractedLicenseInfo(LICENSE_ID1, LICENSE_TEXT1);
		license2 = new ExtractedLicenseInfo(LICENSE_ID2, LICENSE_TEXT2);
		exception1 = new ListedLicenseException(EXCEPTION_ID1, EXCEPTION_NAME1,
				EXCEPTION_TEXT1);
		exception2 = new ListedLicenseException(EXCEPTION_ID2, EXCEPTION_NAME2,
				EXCEPTION_TEXT2);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHashCode() throws InvalidSPDXAnalysisException {
		SimpleLicensingInfo sameLicId = new ExtractedLicenseInfo(LICENSE_ID1, "different text");
		LicenseException sameExceptionId = new ListedLicenseException(EXCEPTION_ID1, "different Name",
				"different exception text"); 
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		WithExceptionOperator weo2 = new WithExceptionOperator(license2, exception2);
		WithExceptionOperator weoSameIdAs1 = new WithExceptionOperator(sameLicId, sameExceptionId);
		assertFalse(weo1.hashCode() == weo2.hashCode());
		assertTrue(weo1.hashCode() == weoSameIdAs1.hashCode());
	}

	public void testEqualsObject() throws InvalidSPDXAnalysisException {
		SimpleLicensingInfo sameLicId = new ExtractedLicenseInfo(LICENSE_ID1, "different text");
		LicenseException sameExceptionId = new ListedLicenseException(EXCEPTION_ID1, "different Name",
				"different exception text"); 
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		WithExceptionOperator weo2 = new WithExceptionOperator(license2, exception2);
		WithExceptionOperator weoSameIdAs1 = new WithExceptionOperator(sameLicId, sameExceptionId);
		assertFalse(weo1.equals(weo2));
		assertTrue(weo1.equals(weoSameIdAs1));
	}


	public void testVerify() throws InvalidSPDXAnalysisException {
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		assertEquals(0, weo1.verify().size());
		weo1.setException(null);
		assertEquals(1, weo1.verify().size());
		weo1.setLicense(null);
		assertEquals(2, weo1.verify().size());
	}

	public void testSetLicense() throws InvalidSPDXAnalysisException {
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		ExtractedLicenseInfo lic1 = (ExtractedLicenseInfo)weo1.getLicense();
		LicenseException le1 = weo1.getException();
		assertEquals(LICENSE_ID1, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT1, lic1.getExtractedText());
		assertEquals(EXCEPTION_ID1, le1.getLicenseExceptionId());
		assertEquals(EXCEPTION_TEXT1, le1.getLicenseExceptionText());
		assertEquals(EXCEPTION_NAME1, le1.getName());
		weo1.setLicense(license2);
		lic1 = (ExtractedLicenseInfo)weo1.getLicense();
		le1 = weo1.getException();
		assertEquals(LICENSE_ID2, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT2, lic1.getExtractedText());
		assertEquals(EXCEPTION_ID1, le1.getLicenseExceptionId());
		assertEquals(EXCEPTION_TEXT1, le1.getLicenseExceptionText());
		assertEquals(EXCEPTION_NAME1, le1.getName());
	}



	public void testSetException() throws InvalidSPDXAnalysisException {
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		ExtractedLicenseInfo lic1 = (ExtractedLicenseInfo)weo1.getLicense();
		LicenseException le1 = weo1.getException();
		assertEquals(LICENSE_ID1, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT1, lic1.getExtractedText());
		assertEquals(EXCEPTION_ID1, le1.getLicenseExceptionId());
		assertEquals(EXCEPTION_TEXT1, le1.getLicenseExceptionText());
		assertEquals(EXCEPTION_NAME1, le1.getName());
		weo1.setException(exception2);
		lic1 = (ExtractedLicenseInfo)weo1.getLicense();
		le1 = weo1.getException();
		assertEquals(LICENSE_ID1, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT1, lic1.getExtractedText());
		assertEquals(EXCEPTION_ID2, le1.getLicenseExceptionId());
		assertEquals(EXCEPTION_TEXT2, le1.getLicenseExceptionText());
		assertEquals(EXCEPTION_NAME2, le1.getName());
	}
}
