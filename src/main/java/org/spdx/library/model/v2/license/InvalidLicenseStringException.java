package org.spdx.library.model.v2.license;

import org.spdx.core.InvalidSPDXAnalysisException;

public class InvalidLicenseStringException extends InvalidSPDXAnalysisException {
	private static final long serialVersionUID = -1688466911486933160L;
	public InvalidLicenseStringException(String message) {
		super(message);
	}
	public InvalidLicenseStringException(String message, Throwable inner) {
		super(message, inner);
	}
}
