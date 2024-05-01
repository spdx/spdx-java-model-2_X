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

import java.util.regex.Pattern;

import org.spdx.storage.PropertyDescriptor;


/**
 * Constants which map to the SPDX specifications for versions prior to SPDX Spec version 3.0
 * @author Gary O'Neall
 *
 */
public class SpdxConstantsCompatV2 {
	
	public static final String SPDX_VERSION_2 = "2";

	// Namespaces
	public static final String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String RDFS_NAMESPACE = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String SPDX_NAMESPACE = "http://spdx.org/rdf/terms#";
	public static final String DOAP_NAMESPACE = "http://usefulinc.com/ns/doap#";
	public static final String OWL_NAMESPACE = "http://www.w3.org/2002/07/owl#";
	public static final String RDF_POINTER_NAMESPACE = "http://www.w3.org/2009/pointers#";
	public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema#";
	
	// RDF Properties - within the RDF_NAMESPACE
	public static final PropertyDescriptor RDF_PROP_TYPE = new PropertyDescriptor("type", RDF_NAMESPACE);
	public static final PropertyDescriptor RDF_PROP_RESOURCE = new PropertyDescriptor("resource", RDF_NAMESPACE);
	public static final String[] RDF_PROPERTIES = new String[] {RDF_PROP_TYPE.getName(), RDF_PROP_RESOURCE.getName()};
	
	
	// OWL Properties - within the OWL_NAMESPACE
	public static final PropertyDescriptor PROP_OWL_SAME_AS = new PropertyDescriptor("sameAs", OWL_NAMESPACE);
	public static final String[] OWL_PROPERTIES = new String[] {PROP_OWL_SAME_AS.getName()};
	
	// RDFS Properties - within the RDFS_NAMESPACE
	public static final PropertyDescriptor RDFS_PROP_COMMENT = new PropertyDescriptor("comment", RDFS_NAMESPACE);
	public static final PropertyDescriptor RDFS_PROP_LABEL = new PropertyDescriptor("label", RDFS_NAMESPACE);
	public static final PropertyDescriptor RDFS_PROP_SEE_ALSO = new PropertyDescriptor("seeAlso", RDFS_NAMESPACE);
	public static final String[] RDFS_PROPERTIES = new String[] {RDFS_PROP_COMMENT.getName(), 
			RDFS_PROP_LABEL.getName(), RDFS_PROP_SEE_ALSO.getName()};
	
	// DOAP Class Names - within the DOAP_NAMESPACE
	public static final String CLASS_DOAP_PROJECT = "Project";
	public static final String[] DOAP_CLASSES = {CLASS_DOAP_PROJECT};
	
	// DOAP Project Property Names - within the DOAP_NAMESPACE
	public static final PropertyDescriptor PROP_PROJECT_HOMEPAGE = new PropertyDescriptor("homepage", DOAP_NAMESPACE);
	public static final String[] DOAP_PROPERTIES = new String[] {PROP_PROJECT_HOMEPAGE.getName()};
	
	// Pointer Class Names - with in the RDF_POINTER_NAMESPACE
	public static final String CLASS_POINTER_START_END_POINTER = "StartEndPointer";
	public static final String CLASS_POINTER_BYTE_OFFSET_POINTER = "ByteOffsetPointer";
	public static final String CLASS_POINTER_LINE_CHAR_POINTER = "LineCharPointer";
	public static final String CLASS_POINTER_COMPOUNT_POINTER = "CompoundPointer";
	public static final String CLASS_SINGLE_POINTER = "SinglePointer";
	public static final String[] POINTER_CLASSES = new String[] {
			CLASS_POINTER_START_END_POINTER, CLASS_POINTER_BYTE_OFFSET_POINTER, 
			CLASS_POINTER_LINE_CHAR_POINTER, CLASS_POINTER_COMPOUNT_POINTER, CLASS_SINGLE_POINTER
			};
	
	// Pointer Properties - with in the RDF_POINTER_NAMESPACE
	public static final PropertyDescriptor PROP_POINTER_START_POINTER = new PropertyDescriptor("startPointer", RDF_POINTER_NAMESPACE);
	public static final PropertyDescriptor PROP_POINTER_END_POINTER = new PropertyDescriptor("endPointer", RDF_POINTER_NAMESPACE);
	public static final PropertyDescriptor PROP_POINTER_REFERENCE = new PropertyDescriptor("reference", RDF_POINTER_NAMESPACE);
	public static final PropertyDescriptor PROP_POINTER_OFFSET = new PropertyDescriptor("offset", RDF_POINTER_NAMESPACE);
	public static final PropertyDescriptor PROP_POINTER_LINE_NUMBER = new PropertyDescriptor("lineNumber", RDF_POINTER_NAMESPACE);
	public static final String[] POINTER_PROPERTIES = new String[] {
			PROP_POINTER_START_POINTER.getName(), PROP_POINTER_END_POINTER.getName(),
			PROP_POINTER_REFERENCE.getName(), PROP_POINTER_OFFSET.getName(),
			PROP_POINTER_LINE_NUMBER.getName()
	};
	
	// SPDX Class Names
	public static final String CLASS_SPDX_DOCUMENT = "SpdxDocument";
	public static final String CLASS_SPDX_PACKAGE = "Package";
	public static final String CLASS_SPDX_CREATION_INFO = "CreationInfo";
	public static final String CLASS_SPDX_CHECKSUM = "Checksum";
	public static final String CLASS_SPDX_ANY_LICENSE_INFO = "AnyLicenseInfo";
	public static final String CLASS_SPDX_SIMPLE_LICENSE_INFO = "SimpleLicensingInfo";
	public static final String CLASS_SPDX_CONJUNCTIVE_LICENSE_SET = "ConjunctiveLicenseSet";
	public static final String CLASS_SPDX_DISJUNCTIVE_LICENSE_SET = "DisjunctiveLicenseSet";
	public static final String CLASS_SPDX_EXTRACTED_LICENSING_INFO = "ExtractedLicensingInfo";
	public static final String CLASS_SPDX_LICENSE = "License";
	public static final String CLASS_SPDX_LISTED_LICENSE = "ListedLicense";
	public static final String CLASS_SPDX_LICENSE_EXCEPTION = "LicenseException";
	public static final String CLASS_SPDX_LISTED_LICENSE_EXCEPTION = "ListedLicenseException";
	public static final String CLASS_OR_LATER_OPERATOR = "OrLaterOperator";
	public static final String CLASS_WITH_EXCEPTION_OPERATOR = "WithExceptionOperator";
	public static final String CLASS_SPDX_FILE = "File";
	public static final String CLASS_SPDX_REVIEW = "Review";
	public static final String CLASS_SPDX_VERIFICATIONCODE = "PackageVerificationCode";
	public static final String CLASS_ANNOTATION = "Annotation";
	public static final String CLASS_RELATIONSHIP = "Relationship";
	public static final String CLASS_SPDX_ITEM = "SpdxItem";
	public static final String CLASS_SPDX_ELEMENT = "SpdxElement";
	public static final String CLASS_SPDX_NONE_ELEMENT = "SpdxNoneElement";
	public static final String CLASS_SPDX_NOASSERTION_ELEMENT = "SpdxNoAssertionElement";
	public static final String CLASS_EXTERNAL_DOC_REF = "ExternalDocumentRef";
	public static final String CLASS_SPDX_EXTERNAL_REFERENCE = "ExternalRef";
	public static final String CLASS_SPDX_REFERENCE_TYPE = "ReferenceType";
	public static final String CLASS_SPDX_SNIPPET = "Snippet";
	public static final String CLASS_NONE_LICENSE = "SpdxNoneLicense";
	public static final String CLASS_NOASSERTION_LICENSE = "SpdxNoAssertionLicense";
	public static final String CLASS_EXTERNAL_SPDX_ELEMENT = "ExternalSpdxElement";
	public static final String CLASS_EXTERNAL_EXTRACTED_LICENSE = "ExternalExtractedLicenseInfo";
	public static final String CLASS_CROSS_REF = "CrossRef";
	
	// all classes used including classes in non-SPDX namespaces
	public static final String[] ALL_SPDX_CLASSES = {CLASS_SPDX_DOCUMENT, CLASS_SPDX_PACKAGE, 
			CLASS_SPDX_CREATION_INFO, CLASS_SPDX_CHECKSUM, CLASS_SPDX_ANY_LICENSE_INFO, 
			CLASS_SPDX_SIMPLE_LICENSE_INFO, CLASS_SPDX_CONJUNCTIVE_LICENSE_SET, CLASS_SPDX_DISJUNCTIVE_LICENSE_SET, 
			CLASS_SPDX_EXTRACTED_LICENSING_INFO, CLASS_SPDX_LICENSE, CLASS_SPDX_LISTED_LICENSE, 
			CLASS_SPDX_LICENSE_EXCEPTION, CLASS_SPDX_LISTED_LICENSE_EXCEPTION, CLASS_OR_LATER_OPERATOR, CLASS_WITH_EXCEPTION_OPERATOR,
			CLASS_SPDX_FILE, CLASS_SPDX_REVIEW, CLASS_SPDX_VERIFICATIONCODE, CLASS_ANNOTATION,
			CLASS_RELATIONSHIP, CLASS_SPDX_ITEM, CLASS_SPDX_ELEMENT, 
			CLASS_SPDX_NONE_ELEMENT, CLASS_SPDX_NOASSERTION_ELEMENT, CLASS_EXTERNAL_DOC_REF,
			CLASS_SPDX_EXTERNAL_REFERENCE, CLASS_SPDX_REFERENCE_TYPE, CLASS_SPDX_SNIPPET,
			CLASS_NONE_LICENSE, CLASS_NOASSERTION_LICENSE, CLASS_EXTERNAL_SPDX_ELEMENT,
			CLASS_EXTERNAL_EXTRACTED_LICENSE, CLASS_CROSS_REF,
			// DOAP Namespace
			CLASS_DOAP_PROJECT,
			// RDF Pointer Namespace
			CLASS_POINTER_START_END_POINTER, CLASS_POINTER_BYTE_OFFSET_POINTER, 
			CLASS_POINTER_COMPOUNT_POINTER, CLASS_POINTER_LINE_CHAR_POINTER, CLASS_SINGLE_POINTER};
	
	// classes that use the listed license URI for their namespace
	public static final String[] LISTED_LICENSE_URI_CLASSES = {CLASS_SPDX_LISTED_LICENSE, CLASS_SPDX_LISTED_LICENSE_EXCEPTION};
	
	// Enumeration class names
	public static final String ENUM_FILE_TYPE = "FileType";
	public static final String ENUM_ANNOTATION_TYPE = "AnnotationType";
	public static final String ENUM_CHECKSUM_ALGORITHM_TYPE = "ChecksumAlgorithm";
	public static final String ENUM_REFERENCE_CATEGORY_TYPE = "ReferenceCategory";
	public static final String ENUM_REFERENCE_RELATIONSHIP_TYPE = "RelationshipType";
	public static final String ENUM_PURPOSE = "Purpose";
	// General SPDX Properties
	public static final PropertyDescriptor PROP_VALUE_NONE = new PropertyDescriptor("none", SPDX_NAMESPACE);
	public static final String URI_VALUE_NONE = PROP_VALUE_NONE.toString();
	public static final PropertyDescriptor PROP_VALUE_NOASSERTION = new PropertyDescriptor("noassertion", SPDX_NAMESPACE);
	public static final String URI_VALUE_NOASSERTION = PROP_VALUE_NOASSERTION.toString();
	public static final String SPDX_IDENTIFIER = "SPDXID";
	public static final String EXTERNAL_DOCUMENT_REF_IDENTIFIER = "externalDocumentId";
	
	// SPDX Document Properties
	// The comment property is the RDFS_PROP_COMMENT property in the rdfs namespace
	public static final PropertyDescriptor PROP_SPDX_REVIEWED_BY = new PropertyDescriptor("reviewed", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_SPDX_EXTRACTED_LICENSES = new PropertyDescriptor("hasExtractedLicensingInfo", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_SPDX_VERSION = new PropertyDescriptor("specVersion", SPDX_NAMESPACE); // TODO: Migrate this to PROP_SPDX_SPEC_VERSION in 3.0.  See issue 
	public static final PropertyDescriptor PROP_SPDX_SPEC_VERSION = new PropertyDescriptor("spdxVersion", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_SPDX_CREATION_INFO = new PropertyDescriptor("creationInfo", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_SPDX_PACKAGE = new PropertyDescriptor("describesPackage", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_SPDX_DATA_LICENSE = new PropertyDescriptor("dataLicense", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_SPDX_EXTERNAL_DOC_REF = new PropertyDescriptor("externalDocumentRef", SPDX_NAMESPACE);
	public static final String SPDX_DOCUMENT_ID = "SPDXRef-DOCUMENT";
	public static final PropertyDescriptor PROP_DOCUMENT_NAMESPACE = new PropertyDescriptor("documentNamespace", SPDX_NAMESPACE);
	
	// SPDX Document properties for JSON and YAML files
	public static final PropertyDescriptor PROP_DOCUMENT_DESCRIBES = new PropertyDescriptor("documentDescribes", SPDX_NAMESPACE); //TODO: This is not yet approved in the spec - see issue #
	public static final PropertyDescriptor PROP_DOCUMENT_FILES = new PropertyDescriptor("files", SPDX_NAMESPACE); //TODO: This is not yet approved in the spec - see issue #
	public static final PropertyDescriptor PROP_DOCUMENT_PACKAGES = new PropertyDescriptor("packages", SPDX_NAMESPACE); //TODO: This is not yet approved in the spec - see issue #
	public static final PropertyDescriptor PROP_DOCUMENT_SNIPPETS = new PropertyDescriptor("snippets", SPDX_NAMESPACE); //TODO: This is not yet approved in the spec - see issue #
	public static final PropertyDescriptor PROP_DOCUMENT_RELATIONSHIPS = new PropertyDescriptor("relationships", SPDX_NAMESPACE); //TODO: This is not yet approved in the spec - see issue #
	
	// SPDX CreationInfo Properties
	// The comment property is the RDFS_PROP_COMMENT property in the rdfs namespace
	public static final PropertyDescriptor PROP_CREATION_CREATOR = new PropertyDescriptor("creator", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_CREATION_CREATED = new PropertyDescriptor("created", SPDX_NAMESPACE); // creation timestamp
	public static final PropertyDescriptor PROP_LICENSE_LIST_VERSION = new PropertyDescriptor("licenseListVersion", SPDX_NAMESPACE);
	public static final String CREATOR_PREFIX_PERSON = "Person:";
	public static final String CREATOR_PREFIX_ORGANIZATION = "Organization:";
	public static final String CREATOR_PREFIX_TOOL = "Tool:";
	
	// SPDX Checksum Properties
	public static final PropertyDescriptor PROP_CHECKSUM_ALGORITHM = new PropertyDescriptor("algorithm", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_CHECKSUM_VALUE = new PropertyDescriptor("checksumValue", SPDX_NAMESPACE);
	public static final String ALGORITHM_SHA1 = "SHA1";
	public static final String PROP_CHECKSUM_ALGORITHM_SHA1 = "checksumAlgorithm_sha1";
	
	// SPDX PackageVerificationCode Properties
	public static final PropertyDescriptor PROP_VERIFICATIONCODE_IGNORED_FILES = new PropertyDescriptor("packageVerificationCodeExcludedFile", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_VERIFICATIONCODE_VALUE = new PropertyDescriptor("packageVerificationCodeValue", SPDX_NAMESPACE);

	// SPDX Element Properties 
	public static final PropertyDescriptor PROP_ANNOTATION = new PropertyDescriptor("annotation", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_RELATIONSHIP = new PropertyDescriptor("relationship", SPDX_NAMESPACE);
	
	// SPDX Item Properties 
	public static final PropertyDescriptor PROP_LICENSE_CONCLUDED = new PropertyDescriptor("licenseConcluded", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_COPYRIGHT_TEXT = new PropertyDescriptor("copyrightText", SPDX_NAMESPACE);	
	public static final PropertyDescriptor PROP_LIC_COMMENTS = new PropertyDescriptor("licenseComments", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_LICENSE_DECLARED = new PropertyDescriptor("licenseDeclared", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_ATTRIBUTION_TEXT = new PropertyDescriptor("attributionText", SPDX_NAMESPACE);
	
	// SPDX Package Properties
	public static final PropertyDescriptor PROP_PACKAGE_DECLARED_NAME = new PropertyDescriptor("name", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_FILE_NAME = new PropertyDescriptor("packageFileName", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_CHECKSUM = new PropertyDescriptor("checksum", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_DOWNLOAD_URL = new PropertyDescriptor("downloadLocation", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_SOURCE_INFO = new PropertyDescriptor("sourceInfo", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_DECLARED_LICENSE = new PropertyDescriptor("licenseDeclared", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_CONCLUDED_LICENSE = PROP_LICENSE_CONCLUDED;
	public static final PropertyDescriptor PROP_PACKAGE_DECLARED_COPYRIGHT = PROP_COPYRIGHT_TEXT;
	public static final PropertyDescriptor PROP_PACKAGE_SHORT_DESC = new PropertyDescriptor("summary", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_DESCRIPTION = new PropertyDescriptor("description", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_FILE = new PropertyDescriptor("hasFile", SPDX_NAMESPACE);;
	public static final PropertyDescriptor PROP_PACKAGE_VERIFICATION_CODE = new PropertyDescriptor("packageVerificationCode", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_LICENSE_INFO_FROM_FILES = new PropertyDescriptor("licenseInfoFromFiles", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_LICENSE_COMMENT = new PropertyDescriptor("licenseComments", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_VERSION_INFO = new PropertyDescriptor("versionInfo", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_ORIGINATOR = new PropertyDescriptor("originator", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_SUPPLIER = new PropertyDescriptor("supplier", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PACKAGE_FILES_ANALYZED = new PropertyDescriptor("filesAnalyzed", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_EXTERNAL_REF = new PropertyDescriptor("externalRef", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_PRIMARY_PACKAGE_PURPOSE = new PropertyDescriptor("primaryPackagePurpose", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_BUILT_DATE = new PropertyDescriptor("builtDate", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_RELEASE_DATE = new PropertyDescriptor("releaseDate", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_VALID_UNTIL_DATE = new PropertyDescriptor("validUntilDate", SPDX_NAMESPACE);
	public static final Pattern REFERENCE_TYPE_URI_PATTERN = Pattern.compile("https?://spdx.org/rdf/references/.+");
	
	// SPDX License Properties
	// The comment property is the RDFS_PROP_COMMENT property in the rdfs namespace
	// the seeAlso property is in the RDFS_PROP_SEE_ALSO property in the rdfs namespace
	public static final PropertyDescriptor PROP_LICENSE_ID = new PropertyDescriptor("licenseId", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_LICENSE_TEXT = new PropertyDescriptor("licenseText", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_LICENSE_TEXT_HTML = new PropertyDescriptor("licenseTextHtml", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_EXTRACTED_TEXT = new PropertyDescriptor("extractedText", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_LICENSE_NAME = new PropertyDescriptor("licenseName", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_STD_LICENSE_NAME_VERSION_1 = new PropertyDescriptor("licenseName", SPDX_NAMESPACE);	// old property name (pre 1.1 spec)
	public static final PropertyDescriptor PROP_STD_LICENSE_NAME = new PropertyDescriptor("name", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_STD_LICENSE_URL_VERSION_1 = new PropertyDescriptor("licenseSourceUrl", SPDX_NAMESPACE);	// This has been replaced with the rdfs:seeAlso property
	public static final PropertyDescriptor PROP_STD_LICENSE_NOTES_VERSION_1 = new PropertyDescriptor("licenseNotes", SPDX_NAMESPACE);	// old property name (pre 1.1 spec)
	public static final PropertyDescriptor PROP_STD_LICENSE_HEADER_VERSION_1 = new PropertyDescriptor("licenseHeader", SPDX_NAMESPACE);	// old property name (pre 1.1 spec)
	public static final PropertyDescriptor PROP_STD_LICENSE_NOTICE = new PropertyDescriptor("standardLicenseHeader", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_STD_LICENSE_HEADER_TEMPLATE = new PropertyDescriptor("standardLicenseHeaderTemplate", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_LICENSE_HEADER_HTML = new PropertyDescriptor("standardLicenseHeaderHtml", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_STD_LICENSE_TEMPLATE_VERSION_1 = new PropertyDescriptor("licenseTemplate", SPDX_NAMESPACE);		// old property name (pre 1.2 spec)
	public static final PropertyDescriptor PROP_STD_LICENSE_TEMPLATE = new PropertyDescriptor("standardLicenseTemplate", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_STD_LICENSE_OSI_APPROVED = new PropertyDescriptor("isOsiApproved", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_STD_LICENSE_FSF_LIBRE = new PropertyDescriptor("isFsfLibre", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_STD_LICENSE_OSI_APPROVED_VERSION_1 = new PropertyDescriptor("licenseOsiApproved", SPDX_NAMESPACE);	// old property name (pre 1.1 spec)
	public static final PropertyDescriptor PROP_LICENSE_SET_MEMEBER = new PropertyDescriptor("member", SPDX_NAMESPACE);
	public static final String TERM_LICENSE_NOASSERTION = PROP_VALUE_NOASSERTION.getName();
	public static final String TERM_LICENSE_NONE = PROP_VALUE_NONE.getName();
	public static final PropertyDescriptor PROP_LICENSE_EXCEPTION_ID = new PropertyDescriptor("licenseExceptionId", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_EXAMPLE = new PropertyDescriptor("example", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_EXCEPTION_TEXT = new PropertyDescriptor("licenseExceptionText", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_EXCEPTION_TEXT_HTML = new PropertyDescriptor("exceptionTextHtml", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_EXCEPTION_TEMPLATE = new PropertyDescriptor("licenseExceptionTemplate", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_LICENSE_EXCEPTION = new PropertyDescriptor("licenseException", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_LIC_ID_DEPRECATED = new PropertyDescriptor("isDeprecatedLicenseId", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_LIC_DEPRECATED_VERSION = new PropertyDescriptor("deprecatedVersion", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_CROSS_REF = new PropertyDescriptor("crossRef", SPDX_NAMESPACE);
	
	// SPDX Listed License constants
	public static final String LISTED_LICENSE_URL = "https://spdx.org/licenses/";
	// http rather than https since RDF depends on the exact string, 
	// we were not able to update the namespace variable to match the URL's.
	public static final String LISTED_LICENSE_NAMESPACE_PREFIX = "http://spdx.org/licenses/";
	
	// crossrefs details (crossRef) properties
	public static final PropertyDescriptor PROP_CROSS_REF_IS_VALID = new PropertyDescriptor("isValid", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_CROSS_REF_WAYBACK_LINK = new PropertyDescriptor("isWayBackLink", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_CROSS_REF_MATCH = new PropertyDescriptor("match", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_CROSS_REF_URL = new PropertyDescriptor("url", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_CROSS_REF_IS_LIVE = new PropertyDescriptor("isLive", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_CROSS_REF_TIMESTAMP = new PropertyDescriptor("timestamp", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_CROSS_REF_ORDER = new PropertyDescriptor("order", SPDX_NAMESPACE);
	
	// SpdxElement Properties
	public static final PropertyDescriptor PROP_NAME = new PropertyDescriptor("name", SPDX_NAMESPACE);
	
	// SPDX File Properties
	// The comment property is the RDFS_PROP_COMMENT property in the rdfs namespace
	public static final PropertyDescriptor PROP_FILE_NAME = new PropertyDescriptor("fileName", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_FILE_TYPE = new PropertyDescriptor("fileType", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_FILE_LICENSE = PROP_LICENSE_CONCLUDED;
	public static final PropertyDescriptor PROP_FILE_COPYRIGHT = PROP_COPYRIGHT_TEXT;
	public static final PropertyDescriptor PROP_FILE_CHECKSUM = new PropertyDescriptor("checksum", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_FILE_SEEN_LICENSE = new PropertyDescriptor("licenseInfoInFile", SPDX_NAMESPACE);	
	public static final PropertyDescriptor PROP_FILE_LIC_COMMENTS = PROP_LIC_COMMENTS;
	public static final PropertyDescriptor PROP_FILE_ARTIFACTOF = new PropertyDescriptor("artifactOf", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_FILE_FILE_DEPENDENCY = new PropertyDescriptor("fileDependency", SPDX_NAMESPACE); 
	public static final PropertyDescriptor PROP_FILE_CONTRIBUTOR = new PropertyDescriptor("fileContributor", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_FILE_NOTICE = new PropertyDescriptor("noticeText", SPDX_NAMESPACE);
	
	// SPDX Snippet Properties
	public static final PropertyDescriptor PROP_SNIPPET_FROM_FILE = new PropertyDescriptor("snippetFromFile", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_SNIPPET_RANGE = new PropertyDescriptor("range", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_LICENSE_INFO_FROM_SNIPPETS = new PropertyDescriptor("licenseInfoInSnippet", SPDX_NAMESPACE);
	
	// SPDX File Type Properties
	public static final String PROP_FILE_TYPE_SOURCE = "fileType_source";
	public static final String PROP_FILE_TYPE_ARCHIVE = "fileType_archive";
	public static final String PROP_FILE_TYPE_BINARY = "fileType_binary";
	public static final String PROP_FILE_TYPE_OTHER = "fileType_other";
	
	public static final String FILE_TYPE_SOURCE = "SOURCE";
	public static final String FILE_TYPE_ARCHIVE = "ARCHIVE";
	public static final String FILE_TYPE_BINARY = "BINARY";
	public static final String FILE_TYPE_OTHER = "OTHER";
	
	// SPDX Annotation Properties
	public static final PropertyDescriptor PROP_ANNOTATOR = new PropertyDescriptor("annotator", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_ANNOTATION_DATE = new PropertyDescriptor("annotationDate", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_ANNOTATION_TYPE = new PropertyDescriptor("annotationType", SPDX_NAMESPACE);
	
	// SPDX Relationship Properties
	public static final PropertyDescriptor PROP_RELATED_SPDX_ELEMENT = new PropertyDescriptor("relatedSpdxElement", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_RELATIONSHIP_TYPE = new PropertyDescriptor("relationshipType", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_SPDX_ELEMENTID = new PropertyDescriptor("spdxElementId", SPDX_NAMESPACE);
	
	// ExternalDocumentRef properties
	public static final PropertyDescriptor PROP_EXTERNAL_DOC_CHECKSUM = new PropertyDescriptor("checksum", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_EXTERNAL_SPDX_DOCUMENT = new PropertyDescriptor("spdxDocument", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_EXTERNAL_DOCUMENT_ID = new PropertyDescriptor("externalDocumentId", SPDX_NAMESPACE);
	
	// External Reference properties
	public static final PropertyDescriptor PROP_REFERENCE_CATEGORY = new PropertyDescriptor("referenceCategory", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_REFERENCE_TYPE = new PropertyDescriptor("referenceType", SPDX_NAMESPACE);
	public static final PropertyDescriptor PROP_REFERENCE_LOCATOR = new PropertyDescriptor("referenceLocator", SPDX_NAMESPACE);
	
	// Date format - NOTE: This format does not handle milliseconds.  Use Instant.parse for full ISO 8601 parsing
	public static final String SPDX_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	// license ID format
	public static String NON_STD_LICENSE_ID_PRENUM = "LicenseRef-";
	public static Pattern LICENSE_ID_PATTERN_NUMERIC = 
			Pattern.compile(NON_STD_LICENSE_ID_PRENUM+"(\\d+)$");	// Pattern for numeric only license IDs
	public static Pattern LICENSE_ID_PATTERN = Pattern.compile(NON_STD_LICENSE_ID_PRENUM+"([0-9a-zA-Z\\.\\-\\_]+)\\+?$");
	
	// SPDX Element Reference format
	public static String SPDX_ELEMENT_REF_PRENUM = "SPDXRef-";
	public static Pattern SPDX_ELEMENT_REF_PATTERN = Pattern.compile(SPDX_ELEMENT_REF_PRENUM+"([0-9a-zA-Z\\.\\-\\+]+)$");
	
	// External Document ID format
	public static String EXTERNAL_DOC_REF_PRENUM = "DocumentRef-";
	public static Pattern EXTERNAL_DOC_REF_PATTERN = Pattern.compile(EXTERNAL_DOC_REF_PRENUM+"([0-9a-zA-Z\\.\\-\\+]+)$");
	public static Pattern EXTERNAL_ELEMENT_REF_PATTERN = Pattern.compile("(.+[0-9a-zA-Z\\.\\-\\+]+):("+SPDX_ELEMENT_REF_PRENUM+"[0-9a-zA-Z\\.\\-\\+]+)$");	
	public static Pattern EXTERNAL_SPDX_ELEMENT_URI_PATTERN = Pattern.compile("(.+)#("+SPDX_ELEMENT_REF_PRENUM+"[0-9a-zA-Z\\.\\-\\+]+)$");
	public static Pattern EXTERNAL_EXTRACTED_LICENSE_URI_PATTERN = Pattern.compile("(.+)#("+NON_STD_LICENSE_ID_PRENUM+"[0-9a-zA-Z\\.\\-\\+]+)$");
	public static Pattern EXTERNAL_EXTRACTED_LICENSE_PATTERN = Pattern.compile("(.+[0-9a-zA-Z\\.\\-\\+]+):("+NON_STD_LICENSE_ID_PRENUM+"[0-9a-zA-Z\\.\\-\\+]+)$");	
	
	// SPDX version format
	public static Pattern SPDX_VERSION_PATTERN = Pattern.compile("^SPDX-(\\d+)\\.(\\d+)$");
	
	// Download Location Format
	private static final String SUPPORTED_DOWNLOAD_REPOS = "(git|hg|svn|bzr)";
	private static final String URL_PATTERN = "(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/|ssh:\\/\\/|git:\\/\\/|svn:\\/\\/|sftp:\\/\\/|ftp:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+){0,100}\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?";
	private static final String GIT_PATTERN = "(git\\+git@[a-zA-Z0-9\\.\\-]+:[a-zA-Z0-9/\\\\.@\\-]+)";
	private static final String BAZAAR_PATTERN = "(bzr\\+lp:[a-zA-Z0-9\\.\\-]+)";
	public static final Pattern DOWNLOAD_LOCATION_PATTERN = Pattern.compile("^(NONE|NOASSERTION|(("+SUPPORTED_DOWNLOAD_REPOS+"\\+)?"+URL_PATTERN+")|"+GIT_PATTERN+"|"+BAZAAR_PATTERN+")$", Pattern.CASE_INSENSITIVE);

	// License list version Format

    public static final Pattern LICENSE_LIST_VERSION_PATTERN = Pattern.compile("^[a-zA-Z0-9]+\\.[a-zA-Z0-9]+");
	// Standard value strings
	public static String NONE_VALUE = "NONE";
	public static String NOASSERTION_VALUE = "NOASSERTION";
	public static final String[] LITERAL_VALUES = new String[]{NONE_VALUE, NOASSERTION_VALUE};
	
	// data license ID
	public static final String SPDX_DATA_LICENSE_ID_VERSION_1_0 = "PDDL-1.0";
	public static final String SPDX_DATA_LICENSE_ID = "CC0-1.0";
	
	public static final String SPDX_LISTED_REFERENCE_TYPES_PREFIX = "http://spdx.org/rdf/references/";
	
	// License XML constants
	public static final String LICENSEXML_URI = "http://www.spdx.org/license";
	public static final String LICENSEXML_ELEMENT_LICENSE_COLLECTION = "SPDXLicenseCollection";
	public static final String LICENSEXML_ELEMENT_LICENSE = "license";
	public static final String LICENSEXML_ELEMENT_EXCEPTION = "exception";
	public static final String LICENSEXML_ATTRIBUTE_ID = "licenseId";
	public static final String LICENSEXML_ATTRIBUTE_DEPRECATED = "isDeprecated";
	public static final String LICENSEXML_ATTRIBUTE_DEPRECATED_VERSION = "deprecatedVersion";
	public static final String LICENSEXML_ATTRIBUTE_OSI_APPROVED = "isOsiApproved";
	public static final String LICENSEXML_ATTRIBUTE_FSF_LIBRE = "isFsfLibre";
	public static final String LICENSEXML_ATTRIBUTE_NAME = "name";
	public static final String LICENSEXML_ATTRIBUTE_LIST_VERSION_ADDED = "listVersionAdded";
	public static final String LICENSEXML_ELEMENT_CROSS_REFS = "crossRefs";
	public static final String LICENSEXML_ELEMENT_CROSS_REF = "crossRef";
	public static final String LICENSEXML_ELEMENT_NOTES = "notes";
	public static final String LICENSEXML_ELEMENT_STANDARD_LICENSE_HEADER = "standardLicenseHeader";
	public static final String LICENSEXML_ELEMENT_TITLE_TEXT = "titleText";
	public static final String LICENSEXML_ELEMENT_COPYRIGHT_TEXT = "copyrightText";
	public static final String LICENSEXML_ELEMENT_BULLET = "bullet";
	public static final String LICENSEXML_ELEMENT_LIST = "list";
	public static final String LICENSEXML_ELEMENT_ITEM = "item";
	public static final String LICENSEXML_ELEMENT_PARAGRAPH = "p";
	public static final String LICENSEXML_ELEMENT_OPTIONAL = "optional";
	public static final String LICENSEXML_ELEMENT_ALT = "alt";
	public static final String LICENSEXML_ATTRIBUTE_ALT_NAME = "name";
	public static final String LICENSEXML_ATTRIBUTE_ALT_MATCH = "match";
	public static final String LICENSEXML_ELEMENT_BREAK = "br";
	public static final String LICENSEXML_ELEMENT_TEXT = "text";

	public static final String SPEC_POINT_EIGHT_SPDX_VERSION = "SPDX-0.8";
	public static final String SPEC_POINT_NINE_SPDX_VERSION = "SPDX-0.9";
	public static final String SPEC_ONE_DOT_ZERO_SPDX_VERSION = "SPDX-1.0";
	public static final String SPEC_ONE_DOT_ONE_SPDX_VERSION = "SPDX-1.1";
	public static final String SPEC_ONE_DOT_TWO_SPDX_VERSION = "SPDX-1.2";
	public static final String SPEC_TWO_POINT_ZERO_VERSION = "SPDX-2.0";
	public static final String SPEC_TWO_POINT_ONE_VERSION = "SPDX-2.1";
	public static final String SPEC_TWO_POINT_TWO_VERSION = "SPDX-2.2";
	public static final String SPEC_TWO_POINT_THREE_VERSION = "SPDX-2.3";
	public static final String SPEC_THREE_POINT_ZERO_VERSION = "SPDX-3.0";
}