package org.spdx.library.model.compat.v2.license;

import org.spdx.core.DefaultModelStore;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelRegistry;
import org.spdx.library.model.compat.v2.MockCopyManager;
import org.spdx.library.model.compat.v2.MockModelStore;
import org.spdx.library.model.v2.GenericSpdxItem;
import org.spdx.library.model.v2.SpdxModelInfoV2_X;
import org.spdx.library.model.v2.license.AnyLicenseInfo;
import org.spdx.library.model.v2.license.SpdxNoneLicense;
import org.spdx.storage.IModelStore;

import junit.framework.TestCase;

public class SpdxNoneLicenseTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		ModelRegistry.getModelRegistry().registerModel(new SpdxModelInfoV2_X());
		DefaultModelStore.initialize(new MockModelStore(), "http://defaultdocument", new MockCopyManager());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHashCodeEquals() throws InvalidSPDXAnalysisException {
		SpdxNoneLicense l1 = new SpdxNoneLicense();
		IModelStore store = new MockModelStore();
		SpdxNoneLicense l2 = new SpdxNoneLicense(store, "https://doc.uri");
		assertEquals(l1.hashCode(), l2.hashCode());
		assertEquals(l1, l2);
		assertTrue(l1.equals(l2));
		assertTrue(l2.equals(l1));
	}
	
	public void testStoreRetrieveNoneLicense() throws InvalidSPDXAnalysisException {
		GenericSpdxItem item = new GenericSpdxItem();
		item.setLicenseConcluded(new SpdxNoneLicense());
		AnyLicenseInfo result = item.getLicenseConcluded();
		SpdxNoneLicense expected = new SpdxNoneLicense();
		assertEquals(expected, result);
	}
}
