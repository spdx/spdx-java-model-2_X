/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spdx.library.model.compat.v2.license;

import junit.framework.TestCase;
import org.spdx.core.DefaultModelStore;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelRegistry;
import org.spdx.library.model.compat.v2.MockCopyManager;
import org.spdx.library.model.compat.v2.MockModelStore;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;
import org.spdx.library.model.v2.SpdxModelFactoryCompatV2;
import org.spdx.library.model.v2.SpdxModelInfoV2_X;
import org.spdx.library.model.v2.license.ExtractedLicenseInfo;
import org.spdx.library.model.v2.license.InvalidLicenseExpression;
import org.spdx.storage.IModelStore;

import java.util.List;

public class InvalidLicenseExpressionTest extends TestCase {

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

    public void testInvalidLicenseExpressionMessageLicenseExpression() throws InvalidSPDXAnalysisException {
        String message = "error message";
        String expression = "license expression";
        String id = DefaultModelStore.getDefaultModelStore().getNextId(IModelStore.IdType.Anonymous);
        InvalidLicenseExpression inv1 = new InvalidLicenseExpression(DefaultModelStore.getDefaultModelStore(), DefaultModelStore.getDefaultDocumentUri(),
                id, DefaultModelStore.getDefaultCopyManager(), message, expression);

        InvalidLicenseExpression inv2 = (InvalidLicenseExpression) SpdxModelFactoryCompatV2.createModelObjectV2(DefaultModelStore.getDefaultModelStore(), DefaultModelStore.getDefaultDocumentUri(),
                id, InvalidLicenseExpression.INVALID_LICENSE_EXPRESSION_TYPE, DefaultModelStore.getDefaultCopyManager());
        assertEquals(message, inv2.getMessage());
        assertEquals(expression, inv2.getLicenseExpression());
        List<String> verify = inv2.verify();
        assertEquals(1, verify.size());
        assertTrue(verify.get(0).contains(message));
        assertTrue(verify.get(0).contains(expression));
    }

}
