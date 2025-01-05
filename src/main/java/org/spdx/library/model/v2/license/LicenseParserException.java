package org.spdx.library.model.v2.license;

import org.spdx.core.InvalidSPDXAnalysisException;

/**
 * Exception thrown when an error occurs while parsing a license string
 * 
 * @author Gary O'Neall
 */
public class LicenseParserException extends InvalidSPDXAnalysisException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 */
	public LicenseParserException(String msg) {
		super(msg);
	}

	public LicenseParserException(String msg, Throwable inner) {
		super(msg, inner);
	}
}
