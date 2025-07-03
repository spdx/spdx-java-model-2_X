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

package org.spdx.library.model.v2.license;

import org.spdx.core.IModelCopyManager;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.storage.IModelStore;
import org.spdx.storage.PropertyDescriptor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.spdx.library.model.v2.SpdxConstantsCompatV2.SPDX_NAMESPACE;

/**
 * Represents a license expression string which can not be parsed - used for error handling
 */
public class InvalidLicenseExpression extends AnyLicenseInfo {

    public static final PropertyDescriptor MESSAGE_PROPERTY = new PropertyDescriptor("invalidLicenseMessage", SPDX_NAMESPACE);
    public static final PropertyDescriptor LICENSE_EXPRESSION_PROPERTY = new PropertyDescriptor("invalidLicenseExpression", SPDX_NAMESPACE);
    public static final String INVALID_LICENSE_EXPRESSION_TYPE = "InvalidLicenseExpression";
    /**
     * Create a new InvalidLicenseExpression object
     * @param modelStore container which includes the license
     * @param documentUri URI for the SPDX document containing the license
     * @param id identifier for the license
     * @param copyManager if non-null, allows for copying of any properties set which use other model stores or document URI's
     * @param create if true, create the license if it does not exist
     * @throws InvalidSPDXAnalysisException on error
     */
    public InvalidLicenseExpression(IModelStore modelStore, String documentUri, String id,
                                    @Nullable IModelCopyManager copyManager, boolean create)
            throws InvalidSPDXAnalysisException {
        super(modelStore, documentUri, id, copyManager, create);
    }

    /**
     * Create a new InvalidLicenseExpression object and initializes the message and licenseExpression
     * @param modelStore container which includes the license
     * @param documentUri URI for the SPDX document containing the license
     * @param id identifier for the license
     * @param copyManager if non-null, allows for copying of any properties set which use other model stores or document URI's
     * @param message Error message describing the nature of the invalid license expression
     * @param licenseExpression License expression string that caused the error
     * @throws InvalidSPDXAnalysisException on error
     */
    public InvalidLicenseExpression(IModelStore modelStore, String documentUri, String id,
                                    @Nullable IModelCopyManager copyManager, String message,
                                    String licenseExpression) throws InvalidSPDXAnalysisException {
        super(modelStore, documentUri, id, copyManager, true);
        setMessage(message);
        setLicenseExpression(licenseExpression);
    }

    @Override
    protected List<String> _verify(Set<String> verifiedElementIds, String specVersion) {
        List<String> retval = new ArrayList<>();
        try {
            retval.add(String.format("Invalid license expression '%s': %s",
                    getLicenseExpression(), getMessage()));
        } catch(Exception e) {
            retval.add(String.format("Error getting properties: %s", e.getMessage()));
        }
        return retval;
    }

    @Override
    public String getType() {
        return INVALID_LICENSE_EXPRESSION_TYPE;
    }

    @Override
    public String toString() {
        try {
            return getLicenseExpression();
        } catch (InvalidSPDXAnalysisException e) {
            return "";
        }
    }

    /**
     * @return the error message associated with the license expression
     * @throws InvalidSPDXAnalysisException on storage related errors
     */
    public String getMessage() throws InvalidSPDXAnalysisException {
        Optional<String> o = getStringPropertyValue(MESSAGE_PROPERTY);
        return o.orElse("[Error message not set]");
    }

    /**
     * @param message the message to set
     * @throws InvalidSPDXAnalysisException on storage related errors
     */
    public void setMessage(String message) throws InvalidSPDXAnalysisException {
        setPropertyValue(MESSAGE_PROPERTY, message);
    }

    /**
     * @return the license expression which had parsing errors
     * @throws InvalidSPDXAnalysisException on storage related errors
     */
    public String getLicenseExpression() throws InvalidSPDXAnalysisException {
        Optional<String> o = getStringPropertyValue(LICENSE_EXPRESSION_PROPERTY);
        return o.orElse("[Error message not set]");
    }

    /**
     * @param expression the license expression to set
     * @throws InvalidSPDXAnalysisException on storage related errors
     */
    public void setLicenseExpression(String expression) throws InvalidSPDXAnalysisException {
        setPropertyValue(LICENSE_EXPRESSION_PROPERTY, expression);
    }
}
