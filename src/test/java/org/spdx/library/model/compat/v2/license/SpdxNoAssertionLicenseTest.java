package org.spdx.library.model.compat.v2.license;

import org.spdx.core.DefaultModelStore;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelRegistry;
import org.spdx.library.model.compat.v2.MockCopyManager;
import org.spdx.library.model.compat.v2.MockModelStore;
import org.spdx.library.model.v2.SpdxModelInfoV2_X;
import org.spdx.library.model.v2.license.SpdxNoAssertionLicense;
import org.spdx.storage.IModelStore;

import junit.framework.TestCase;

public class SpdxNoAssertionLicenseTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		ModelRegistry.getModelRegistry().registerModel(new SpdxModelInfoV2_X());
		DefaultModelStore.initialize(new MockModelStore(), "http://defaultdocument", new MockCopyManager());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHashCodeEquals() throws InvalidSPDXAnalysisException {
		SpdxNoAssertionLicense l1 = new SpdxNoAssertionLicense();
		IModelStore store = new MockModelStore();
		SpdxNoAssertionLicense l2 = new SpdxNoAssertionLicense(store, "https://doc.uri");
		assertEquals(l1.hashCode(), l2.hashCode());
		assertEquals(l1, l2);
		assertTrue(l1.equals(l2));
		assertTrue(l2.equals(l1));
	}

}
