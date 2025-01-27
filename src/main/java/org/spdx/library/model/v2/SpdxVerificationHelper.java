/**
 * Copyright (c) 2011 Source Auditor Inc.
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
 *
*/
package org.spdx.library.model.v2;

import java.net.URI;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spdx.library.model.v2.enumerations.ChecksumAlgorithm;

/**
 * Holds static methods used for verify various property values
 *
 * @author Gary O'Neall
 */
public class SpdxVerificationHelper {
	
	public static final List<String> SUPPORTED_SPDX_VERSIONS = Collections.unmodifiableList(new ArrayList<String>(Arrays.asList(new String[]{
			SpdxConstantsCompatV2.SPEC_TWO_POINT_ZERO_VERSION,
			SpdxConstantsCompatV2.SPEC_TWO_POINT_ONE_VERSION, 
			SpdxConstantsCompatV2.SPEC_TWO_POINT_TWO_VERSION, 
			SpdxConstantsCompatV2.SPEC_TWO_POINT_THREE_VERSION})));
	
	static final Map<ChecksumAlgorithm, Integer> CHECKSUM_VALUE_LENGTH;
	static {
		Map<ChecksumAlgorithm, Integer> map = new HashMap<>();
		map.put(ChecksumAlgorithm.ADLER32, 8);
		map.put(ChecksumAlgorithm.BLAKE2b_256, 256 / 4);
		map.put(ChecksumAlgorithm.BLAKE2b_384, 384 / 4);
		map.put(ChecksumAlgorithm.BLAKE2b_512, 512 / 4);
		map.put(ChecksumAlgorithm.BLAKE3, 256 / 4); // Assuming this is the default output size
		map.put(ChecksumAlgorithm.MD2, 128 / 4);
		map.put(ChecksumAlgorithm.MD4, 128 / 4);
		map.put(ChecksumAlgorithm.MD5, 128 / 4);
		map.put(ChecksumAlgorithm.MD6, null); // variable 0 to 128 bits
		map.put(ChecksumAlgorithm.SHA1, 40);
		map.put(ChecksumAlgorithm.SHA224, 224 / 4);
		map.put(ChecksumAlgorithm.SHA256, 64);
		map.put(ChecksumAlgorithm.SHA384, 384 / 4);
		map.put(ChecksumAlgorithm.SHA3_256, 256 / 4);
		map.put(ChecksumAlgorithm.SHA3_384, 384 / 4);
		map.put(ChecksumAlgorithm.SHA3_512, 512 / 4);
		map.put(ChecksumAlgorithm.SHA512, 512 / 4);
		CHECKSUM_VALUE_LENGTH = Collections.unmodifiableMap(map);
	}
	static final Set<ChecksumAlgorithm> CHECKSUM_ALGORITHMS_ADDED_23;
	static {
		Set<ChecksumAlgorithm> set = new HashSet<>();
		set.add(ChecksumAlgorithm.SHA3_256);
		set.add(ChecksumAlgorithm.SHA3_384);
		set.add(ChecksumAlgorithm.SHA3_512);
		set.add(ChecksumAlgorithm.BLAKE2b_256);
		set.add(ChecksumAlgorithm.BLAKE2b_384);
		set.add(ChecksumAlgorithm.BLAKE2b_512);
		set.add(ChecksumAlgorithm.BLAKE3);
		set.add(ChecksumAlgorithm.ADLER32);
		CHECKSUM_ALGORITHMS_ADDED_23 = Collections.unmodifiableSet(set);
	}
	
	static final String[] VALID_CREATOR_PREFIXES = new String[] {SpdxConstantsCompatV2.CREATOR_PREFIX_PERSON,
		SpdxConstantsCompatV2.CREATOR_PREFIX_ORGANIZATION, SpdxConstantsCompatV2.CREATOR_PREFIX_TOOL};
	static final String[] VALID_ORIGINATOR_SUPPLIER_PREFIXES = new String[] {SpdxConstantsCompatV2.NOASSERTION_VALUE, "Person:", "Organization:"};
	static final Pattern SPDX_ELEMENT_ID_PATTERN = Pattern.compile(".*" + SpdxConstantsCompatV2.SPDX_ELEMENT_REF_PRENUM+"([0-9a-zA-Z\\.\\-\\+]+)$");
	static final Pattern LICENSE_ID_PATTERN = Pattern.compile(".*" + SpdxConstantsCompatV2.NON_STD_LICENSE_ID_PRENUM+"([0-9a-zA-Z\\.\\-\\_]+)\\+?$");
	static final Pattern EXTERNAL_DOC_REF_PATTERN = Pattern.compile(".*" + SpdxConstantsCompatV2.EXTERNAL_DOC_REF_PRENUM+"([0-9a-zA-Z\\.\\-\\+]+)$");; 
	
	public static String verifyNonStdLicenseId(String licenseUri) {
		if (LICENSE_ID_PATTERN.matcher(licenseUri).matches()) {
			return null;
		} else {
			return "Invalid license objectUri '"+licenseUri+"'.  Must start with 'LicenseRef-' " +
					"and made up of the characters from the set 'a'-'z', 'A'-'Z', '0'-'9', '+', '_', '.', and '-'.";
		}
	}
	
	/**
	 * Verifies a creator string value
	 * @param creator
	 * @return
	 */
	public static String verifyCreator(String creator) {
		boolean ok = false;
		for (int i = 0; i < VALID_CREATOR_PREFIXES.length; i++) {
			if (creator.startsWith(VALID_CREATOR_PREFIXES[i])) {
				ok = true;
				break;
			}
		}
		if (!ok) {
			StringBuilder sb = new StringBuilder("Creator does not start with one of ");
			sb.append(VALID_CREATOR_PREFIXES[0]);
			for (int i = 1; i < VALID_CREATOR_PREFIXES.length; i++) {
				sb.append(", ");
				sb.append(VALID_CREATOR_PREFIXES[i]);
			}
			return sb.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * Verifies the originator string
	 * @param originator
	 * @return
	 */
	public static String verifyOriginator(String originator) {
		return verifyOriginatorOrSupplier(originator);
	}
	
	/**
	 * Verifies the supplier String
	 * @param supplier
	 * @return
	 */
	public static String verifySupplier(String supplier) {
		return verifyOriginatorOrSupplier(supplier);
	}

	@Deprecated
	public static String verifyNonStdLicenseid(String licenseUri) {
		return verifyNonStdLicenseId(licenseUri);
	}

	/**
	 * Verifies a the originator or supplier
	 * @param creator
	 * @return
	 */
	private static String verifyOriginatorOrSupplier(String originatorOrSupplier) {
		boolean ok = false;
		for (int i = 0; i < VALID_ORIGINATOR_SUPPLIER_PREFIXES.length; i++) {
			if (originatorOrSupplier.startsWith(VALID_ORIGINATOR_SUPPLIER_PREFIXES[i])) {
				ok = true;
				break;
			}
		}
		if (!ok) {
			StringBuilder sb = new StringBuilder("Value must start with one of ");
			sb.append(VALID_ORIGINATOR_SUPPLIER_PREFIXES[0]);
			for (int i = 1; i < VALID_ORIGINATOR_SUPPLIER_PREFIXES.length; i++) {
				sb.append(", ");
				sb.append(VALID_ORIGINATOR_SUPPLIER_PREFIXES[i]);
			}
			return sb.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * @param creationDate
	 * @return error message or null if no error
	 */
	public static String verifyDate(String creationDate) {
		try {
			Instant.parse(creationDate);
		} catch (DateTimeParseException e) {
			return("Invalid date format: "+e.getMessage());
		}
		return null;
	}

	/**
	 * @param reviewer
	 * @return
	 */
	public static String verifyReviewer(String reviewer) {
		if (!reviewer.startsWith("Person:") && !reviewer.startsWith("Tool:") &&
				!reviewer.startsWith("Organization:")) {
			return "Reviewer does not start with Person:, Organization:, or Tool:";
		} else {
			return null;
		}
	}
	
	/**
	 * Returns true if s1 equals s2 taking into account the possibility of null values
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean equalsWithNull(Object s1, Object s2) {
		if (s1 == null) {
			return (s2 == null);
		}
		if (s2 == null) {
			return false;
		}
		return s1.equals(s2);
	}
	
	/**
	 * Returns true if the array s1 contains the same objects as s2 independent of order
	 * and allowing for null values
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean equivalentArray(Object[] s1, Object[] s2) {
		if (s1 == null) {
			return (s2 == null);
		}
		if (s2 == null) {
			return false;
		}
		if (s1.length != s2.length) {
			return false;
		}
		for (int i = 0; i < s1.length; i++) {
			boolean found = false;
			for (int j = 0; j < s2.length; j++) {
				if (equalsWithNull(s1[i], s2[j])) {
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

	/**
	 * @param annotator
	 * @return
	 */
	public static String verifyAnnotator(String annotator) {
		if (!annotator.startsWith("Person:") && !annotator.startsWith("Tool:") &&
				!annotator.startsWith("Organization:")) {
			return "Annotator does not start with Person:, Organization:, or Tool";
		} else {
			return null;
		}
	}

	/**
	 * @param externalDocumentId
	 * @return
	 */
	public static boolean isValidExternalDocRef(String externalDocumentId) {
		return EXTERNAL_DOC_REF_PATTERN.matcher(externalDocumentId).matches();
	}

	public static boolean isValidUri(String uri) {
		try {
			URI.create(uri);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String verifyChecksumString(String checksum, ChecksumAlgorithm algorithm, String specVersion) {		
		for (int i = 0; i < checksum.length(); i++) {
			char c = checksum.charAt(i);
			if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))) {
				return "Invalid checksum string character at position "+String.valueOf(i);
			}
		}
		
		Integer valueSize = CHECKSUM_VALUE_LENGTH.get(algorithm);
		if (Objects.nonNull(valueSize) && checksum.length() != valueSize) {
			return "Invalid number of characters for checksum";
		}
		if (versionLessThan(specVersion, SpdxConstantsCompatV2.SPEC_TWO_POINT_THREE_VERSION)) {
			if (CHECKSUM_ALGORITHMS_ADDED_23.contains(algorithm)) {
				return "This algorithm is not supported in SPDX specification versions less than 2.3";
			}
		}
		return null;	// if we got here, all OK
	}

	/**
	 * Verify a download location per section 3.7.5 of the spec
	 * @param downloadLocation
	 * @return null if a valid string otherwise a description of the error
	 */
	public static String verifyDownloadLocation(String downloadLocation) {
		if (Objects.isNull(downloadLocation)) {
			return "Download location is null";
		} else if (SpdxConstantsCompatV2.DOWNLOAD_LOCATION_PATTERN.matcher(downloadLocation).matches()) {
			return null;
		} else {
			return "Invalid download location "+downloadLocation+".  Must match the pattern "+
					SpdxConstantsCompatV2.DOWNLOAD_LOCATION_PATTERN.pattern();
		}
	}
	
	/**
	 * @param objectUri
	 * @return true if the ID is a valid SPDX ID reference
	 */
	public static boolean verifySpdxId(String objectUri) {
		return SPDX_ELEMENT_ID_PATTERN.matcher(objectUri).matches();
	}

	/**
	 * Verifies an SPDX version 2.X version string
	 * @param spdxVersion version to verify
	 * @return null if no errors, otherwise a string error message
	 */
	public static String verifySpdxVersion(String spdxVersion) {
		if (!spdxVersion.startsWith("SPDX-")) {
			return "Invalid spdx version - must start with 'SPDX-'";
		}
		Matcher docSpecVersionMatcher = SpdxConstantsCompatV2.SPDX_VERSION_PATTERN.matcher(spdxVersion);
		if (!docSpecVersionMatcher.matches()) {
			return "Invalid spdx version format - must match 'SPDX-M.N'";
		}
		if (!SUPPORTED_SPDX_VERSIONS.contains(spdxVersion)) {
			return "Version "+spdxVersion+" is not supported by this version of the rdf parser";
		}
		return null;	// if we got here, there is no problem
	}

	/**
	 * Compares versions of the SPDX spec
	 * @param specVersion
	 * @param compareSpecVersion
	 * @return true if specVersion is less than compareSpecVersion
	 */
	public static boolean versionLessThan(String specVersion,
			String compareSpecVersion) {
		int i = SUPPORTED_SPDX_VERSIONS.indexOf(specVersion);
		if (i < 0) {
			return false;
		}
		int j = SUPPORTED_SPDX_VERSIONS.indexOf(compareSpecVersion);
		if (j < 0) {
			return false;
		}
		return i < j;
	}
}
