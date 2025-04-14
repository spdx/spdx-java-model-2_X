/**
 * Copyright (c) 2023 Source Auditor Inc.
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
package org.spdx.storage.compatv2;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelRegistryException;
import org.spdx.core.SpdxCoreConstants;
import org.spdx.core.SpdxInvalidIdException;
import org.spdx.core.SpdxInvalidTypeException;
import org.spdx.core.TypedValue;
import org.spdx.storage.IModelStore;
import org.spdx.storage.PropertyDescriptor;

/**
 * Wraps a model store providing a compatible interface to the 1.X version of the SPDX Java Library
 *
 * @author Gary O'Neall
 */
public class CompatibleModelStoreWrapper implements IModelStore {
	
	public static class TypedValueCompatV2 {
		String id;
		String type;
		
		public TypedValueCompatV2(String id, String type) {
			this.id = id;
			this.type = type;
		}
	}
	
	public static final String LATEST_SPDX_2X_VERSION = "SPDX-2.3";
	
	// Namespaces
	public static final String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String RDFS_NAMESPACE = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String SPDX_NAMESPACE = "http://spdx.org/rdf/terms#";
	public static final String DOAP_NAMESPACE = "http://usefulinc.com/ns/doap#";
	public static final String OWL_NAMESPACE = "http://www.w3.org/2002/07/owl#";
	public static final String RDF_POINTER_NAMESPACE = "http://www.w3.org/2009/pointers#";
	public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema#";
	
	static final Map<String, String> PROP_NAME_TO_NON_SPDX_NS;
	
	static {
		Map<String, String> nameToNS = new HashMap<>();
		nameToNS.put("type", RDF_NAMESPACE);
		nameToNS.put("resource", RDF_NAMESPACE);
		nameToNS.put("sameAs", OWL_NAMESPACE);
		nameToNS.put("comment", RDFS_NAMESPACE);
		nameToNS.put("label", RDFS_NAMESPACE);
		nameToNS.put("seeAlso", RDFS_NAMESPACE);
		nameToNS.put("homepage", DOAP_NAMESPACE);
		nameToNS.put("startPointer", RDF_POINTER_NAMESPACE);
		nameToNS.put("endPointer", RDF_POINTER_NAMESPACE);
		nameToNS.put("reference", RDF_POINTER_NAMESPACE);
		nameToNS.put("offset", RDF_POINTER_NAMESPACE);
		nameToNS.put("lineNumber", RDF_POINTER_NAMESPACE);
		
		PROP_NAME_TO_NON_SPDX_NS = Collections.unmodifiableMap(nameToNS);
	}
	
	private IModelStore baseStore;
	
	public CompatibleModelStoreWrapper(IModelStore baseStore) {
		Objects.requireNonNull(baseStore, "A base store must be provided for the CompatibleModelStoreWrapper");
		this.baseStore = baseStore;
	}
	
	public static PropertyDescriptor propNameToPropDescriptor(String propName) {
		return new PropertyDescriptor(propName, 
				PROP_NAME_TO_NON_SPDX_NS.getOrDefault(propName, SPDX_NAMESPACE));
	}

	@Override
	public void close() throws Exception {
		baseStore.close();
	}

	/**
	 * @param documentUri a nameSpace for the ID
	 * @param id unique ID within the SPDX document
	 * @return true if the objectUri already exists for the documentUri
	 */
	public boolean exists(String documentUri, String id) {
		return exists(documentUriIdToUri(documentUri, id, baseStore));
	}
	
	/**
	 * @param documentUri SPDX v2 Document URI
	 * @param id ID consistent with SPDX v2 spec
	 * @param store store used for the Document URI
	 * @return true if the objectUri already exists for the documentUri
	 */
	public static String documentUriIdToUri(String documentUri, String id, IModelStore store) {
		return documentUriIdToUri(documentUri, id, store.isAnon(id));
	}
	
	public static String documentUriToNamespace(String documentUri) {
		if (documentUri.contains("://spdx.org/licenses/"))  {
			return documentUri;
		} else {
			return documentUri + "#";
		}
	}
	
	/**
	 * @param documentUri SPDX v2 Document URI
	 * @param id ID consistent with SPDX v2 spec
	 * @param anonymous true of this is an anonymous ID
	 * @return a URI based on the document URI and ID - if anonymous is true, the ID is returned
	 */
	public static String documentUriIdToUri(String documentUri, String id, boolean anonymous) {
		return anonymous ? id : documentUriToNamespace(documentUri) + id;
	}
	
	/**
	 * Convenience method to convert an SPDX 2.X style typed value to the current TypedValue
	 * @param documentUri SPDX v2 Document URI
	 * @param id ID consistent with SPDX v2 spec
	 * @param anonymous true of this is an anonymous ID
	 * @param type SPDX type
	 * @return TypedValue with the proper Object URI formed by the documentUri and ID
	 * @throws SpdxInvalidIdException if the ID is not valid
	 * @throws SpdxInvalidTypeException if the type is not valid
	 * @throws ModelRegistryException if there is no model registered for the spec version
	 */
	public static TypedValue typedValueFromDocUri(String documentUri, String id, boolean anonymous, String type) throws SpdxInvalidIdException, SpdxInvalidTypeException, ModelRegistryException {
		return new TypedValue(documentUriIdToUri(documentUri, id, anonymous), type, LATEST_SPDX_2X_VERSION);
	}
	
	/**
	 * Convenience method to convert an SPDX 2.X style typed value to the current TypedValue
	 * @param documentUri SPDX v2 Document URI
	 * @param id ID consistent with SPDX v2 spec
	 * @param store store used
	 * @param type SPDX type
	 * @return TypedValue with the proper Object URI formed by the documentUri and ID
	 * @throws SpdxInvalidIdException if the ID is not valid
	 * @throws SpdxInvalidTypeException if the type is not valid
	 * @throws ModelRegistryException if there is no model registered for the spec version
	 */
	public static TypedValue typedValueFromDocUri(String documentUri, String id, IModelStore store, String type) throws SpdxInvalidIdException, SpdxInvalidTypeException, ModelRegistryException {
		return new TypedValue(documentUriIdToUri(documentUri, id, store), type, LATEST_SPDX_2X_VERSION);
	}
	
	/**
	 * @param store Store storing the objet URI
	 * @param objectUri Object URI
	 * @param documentUri SPDX 2 document URI for the ID
	 * @return the SPDX 2 compatible ID
	 * @throws InvalidSPDXAnalysisException 
	 */
	public static String objectUriToId(IModelStore store, String objectUri, String documentUri) throws InvalidSPDXAnalysisException {
		return objectUriToId(store.isAnon(objectUri), objectUri, documentUri);
	}
	
	/**
	 * @param anon true if the ID type is anonymous
	 * @param objectUri Object URI
	 * @param documentUri SPDX 2 document URI for the ID
	 * @return the SPDX 2 compatible ID
	 * @throws InvalidSPDXAnalysisException on any SPDX exception
	 */
	public static String objectUriToId(boolean anon, String objectUri, String documentUri) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(objectUri, "Object URI can not be null");
		if (anon) {
			return objectUri;
		}
		if (objectUri.startsWith(SpdxCoreConstants.LISTED_LICENSE_URL)) {
			return objectUri.substring(SpdxCoreConstants.LISTED_LICENSE_URL.length());
		}
		if (objectUri.startsWith(SpdxCoreConstants.LISTED_LICENSE_NAMESPACE_PREFIX)) {
			return objectUri.substring(SpdxCoreConstants.LISTED_LICENSE_NAMESPACE_PREFIX.length());
		}
		if ("http://spdx.org/rdf/terms#noassertion".equals(objectUri)) {
			return "NOASSERTION";
		}
		if ("http://spdx.org/rdf/terms#none".equals(objectUri)) {
			return "NONE";
		}
		Objects.requireNonNull(documentUri, "Document URI can not be null");
		String nameSpace = documentUri + "#";
		if (!objectUri.startsWith(nameSpace)) {
			throw new InvalidSPDXAnalysisException("Object URI must start with document URI + #.  DocumentUri: " +
						documentUri + ", Object URI: "+objectUri);
		}
		return objectUri.substring(nameSpace.length());
	}

	@Override
	public boolean exists(String uri) {
		return baseStore.exists(uri);
	}
	
	/**
	 * @param documentUri SPDX v2 spec document URI
	 * @param id SPDX ID
	 * @param type type
	 * @throws InvalidSPDXAnalysisException on any SPDX exception
	 */
	public void create(String documentUri, String id, String type)
			throws InvalidSPDXAnalysisException {
		baseStore.create(
				new TypedValue(documentUriIdToUri(documentUri, id, this), type, LATEST_SPDX_2X_VERSION));
	}
	
	@Override
	public void create(TypedValue typedValue) throws InvalidSPDXAnalysisException {
		baseStore.create(typedValue);
	}

	@Override
	public List<PropertyDescriptor> getPropertyValueDescriptors(
			String objectUri) throws InvalidSPDXAnalysisException {
		return baseStore.getPropertyValueDescriptors(objectUri);
	}
	
	public List<PropertyDescriptor> getPropertyValueDescriptors(
			String documentUri, String id) throws InvalidSPDXAnalysisException {
		return getPropertyValueDescriptors(documentUriIdToUri(documentUri, id, baseStore));
	}
	
	/**
	 * @param objectUri URI for the item
	 * @return all property names stored for the Object URI
	 * @throws InvalidSPDXAnalysisException on any SPDX exception
	 */
	public Collection<String> getPropertyValueNames(
			String objectUri) throws InvalidSPDXAnalysisException {
		return StreamSupport.stream(getPropertyValueDescriptors(objectUri).spliterator(), false)
						.map(descriptor -> descriptor.getName())
								.collect(Collectors.toList());
	}
	
	/**
	 * @param documentUri document URI
	 * @param id ID for the item
	 * @return all property names stored for the documentUri#id
	 * @throws InvalidSPDXAnalysisException on any SPDX exception
	 */
	public Collection<String> getPropertyValueNames(
			String documentUri, String id) throws InvalidSPDXAnalysisException {
		return getPropertyValueNames(documentUriIdToUri(documentUri, id, baseStore));
	}
	
	@Override
	public void setValue(String objectUri,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		baseStore.setValue(objectUri, propertyDescriptor, value);
	}

	public void setValue(String documentUri, String id,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		setValue(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor, value);
	}
	
	public void setValue(String documentUri, String id,
			String propertyName, Object value)
			throws InvalidSPDXAnalysisException {
		setValue(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName), value);
	}

	@Override
	public Optional<Object> getValue(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		return baseStore.getValue(objectUri, propertyDescriptor);
	}
	
	public Optional<Object> getValue(String documentUri, String id,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		return getValue(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor);
	}
	
	public Optional<Object> getValue(String documentUri, String id,
			String propertyName)
			throws InvalidSPDXAnalysisException {
		return getValue(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName));
	}

	public String getNextId(IdType idType, String documentUri)
			throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(documentUri, "SPDX V2 requires a namespace for generating next ID's");
		return baseStore.getNextId(idType);
	}

	@Override
	public String getNextId(IdType idType)
			throws InvalidSPDXAnalysisException {
		return baseStore.getNextId(idType);
	}
	
	@Override
	public void removeProperty(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		baseStore.removeProperty(objectUri, propertyDescriptor);
	}
	
	public void removeProperty(String documentUri, String id,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		removeProperty(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor);
	}
	
	public void removeProperty(String documentUri, String id,
			String propertyName)
			throws InvalidSPDXAnalysisException {
		removeProperty(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName));
	}

	@Override
	public Stream<TypedValue> getAllItems(String nameSpace, String typeFilter)
			throws InvalidSPDXAnalysisException {
		return baseStore.getAllItems(nameSpace, typeFilter);
	}

	@Override
	public IModelStoreLock enterCriticalSection(boolean readLockRequested) throws InvalidSPDXAnalysisException {
		return baseStore.enterCriticalSection(readLockRequested);
	}

	public IModelStoreLock enterCriticalSection(String documentUri,
			boolean readLockRequested) throws InvalidSPDXAnalysisException {
		return enterCriticalSection(readLockRequested);
	}

	@Override
	public void leaveCriticalSection(IModelStoreLock lock) {
		baseStore.leaveCriticalSection(lock);
	}

	@Override
	public boolean removeValueFromCollection(String objectUri,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		return baseStore.removeValueFromCollection(objectUri, propertyDescriptor, value);
	}
	
	public boolean removeValueFromCollection(String documentUri, String id,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		return removeValueFromCollection(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor, value);
	}
	
	public boolean removeValueFromCollection(String documentUri, String id,
			String propertyName, Object value)
			throws InvalidSPDXAnalysisException {
		return removeValueFromCollection(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName), value);
	}

	@Override
	public int collectionSize(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		return baseStore.collectionSize(objectUri, propertyDescriptor);
	}
	
	public int collectionSize(String documentUri, String id,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		return collectionSize(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor);
	}
	
	public int collectionSize(String documentUri, String id,
			String propertyName)
			throws InvalidSPDXAnalysisException {
		return collectionSize(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName));
	}

	@Override
	public boolean collectionContains(String objectUri,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		return baseStore.collectionContains(objectUri, propertyDescriptor, value);
	}
	
	public boolean collectionContains(String documentUri, String id,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		return collectionContains(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor, value);
	}
	
	public boolean collectionContains(String documentUri, String id,
			String propertyName, Object value)
			throws InvalidSPDXAnalysisException {
		return collectionContains(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName), value);
	}

	@Override
	public void clearValueCollection(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		baseStore.clearValueCollection(objectUri, propertyDescriptor);
	}
	
	public void clearValueCollection(String documentUri, String id,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		clearValueCollection(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor);
	}
	
	public void clearValueCollection(String documentUri, String id,
			String propertyName)
			throws InvalidSPDXAnalysisException {
		clearValueCollection(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName));
	}

	@Override
	public boolean addValueToCollection(String objectUri,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		return baseStore.addValueToCollection(objectUri, propertyDescriptor, value);
	}
	
	public boolean addValueToCollection(String documentUri, String id,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		return addValueToCollection(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor, value);
	}
	
	public boolean addValueToCollection(String documentUri, String id,
			String propertyName, Object value)
			throws InvalidSPDXAnalysisException {
		return addValueToCollection(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName), value);
	}

	@Override
	public Iterator<Object> listValues(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		return baseStore.listValues(objectUri, propertyDescriptor);
	}
	
	public Iterator<Object> listValues(String documentUri, String id,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		return listValues(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor);
	}
	
	public Iterator<Object> listValues(String documentUri, String id,
			String propertyName)
			throws InvalidSPDXAnalysisException {
		return listValues(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName));
	}
	@Override
	public boolean isCollectionMembersAssignableTo(String objectUri,
			PropertyDescriptor propertyDescriptor, Class<?> clazz)
			throws InvalidSPDXAnalysisException {
		return baseStore.isCollectionMembersAssignableTo(objectUri, propertyDescriptor, clazz);
	}
	
	public boolean isCollectionMembersAssignableTo(String documentUri,
			String id, PropertyDescriptor propertyDescriptor, Class<?> clazz)
			throws InvalidSPDXAnalysisException {
		return isCollectionMembersAssignableTo(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor, clazz);
	}
	
	public boolean isCollectionMembersAssignableTo(String documentUri,
			String id, String propertyName, Class<?> clazz)
			throws InvalidSPDXAnalysisException {
		return isCollectionMembersAssignableTo(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName), clazz);
	}
	@Override
	public boolean isPropertyValueAssignableTo(String objectUri,
			PropertyDescriptor propertyDescriptor, Class<?> clazz, String specVersion)
			throws InvalidSPDXAnalysisException {
		return baseStore.isPropertyValueAssignableTo(objectUri, propertyDescriptor, clazz, specVersion);
	}
	
	public boolean isPropertyValueAssignableTo(String documentUri, String id,
			PropertyDescriptor propertyDescriptor, Class<?> clazz)
			throws InvalidSPDXAnalysisException {
		return isPropertyValueAssignableTo(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor, 
				clazz, LATEST_SPDX_2X_VERSION);
	}
	
	public boolean isPropertyValueAssignableTo(String documentUri, String id,
			String propertyName, Class<?> clazz)
			throws InvalidSPDXAnalysisException {
		return isPropertyValueAssignableTo(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName), 
				clazz, LATEST_SPDX_2X_VERSION);
	}

	@Override
	public boolean isCollectionProperty(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		return baseStore.isCollectionProperty(objectUri, propertyDescriptor);
	}
	
	public boolean isCollectionProperty(String documentUri, String id,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		return isCollectionProperty(documentUriIdToUri(documentUri, id, baseStore), propertyDescriptor);
	}
	
	public boolean isCollectionProperty(String documentUri, String id,
			String propertyName)
			throws InvalidSPDXAnalysisException {
		return isCollectionProperty(documentUriIdToUri(documentUri, id, baseStore), propNameToPropDescriptor(propertyName));
	}

	@Override
	public IdType getIdType(String objectUri) {
		return baseStore.getIdType(objectUri);
	}

	@Override
	public Optional<String> getCaseSensitiveId(String documentUri,
			String caseInsensitiveId) {
		return baseStore.getCaseSensitiveId(documentUri, caseInsensitiveId);
	}

	@Override
	public Optional<TypedValue> getTypedValue(String objectUri)
			throws InvalidSPDXAnalysisException {
		return baseStore.getTypedValue(objectUri);
	}
	
	public Optional<TypedValue> getTypedValue(String documentUri, String id)
			throws InvalidSPDXAnalysisException {
		return getTypedValue(documentUriIdToUri(documentUri, id, baseStore));
	}

	@Override
	public void delete(String documentUri)
			throws InvalidSPDXAnalysisException {
		baseStore.delete(documentUri);
	}

	public void delete(String documentUri, String id)
			throws InvalidSPDXAnalysisException {
		delete(documentUriIdToUri(documentUri, id, baseStore));
	}

	/**
	 * @return the store this store wraps
	 */
	public IModelStore getBaseModelStore() {
		return this.baseStore;
	}
	
	@Override
	public boolean equals(Object comp) {
		return comp instanceof CompatibleModelStoreWrapper && getBaseModelStore().equals(((CompatibleModelStoreWrapper)comp).getBaseModelStore());
		// Return true if the base is equal since this contains no properties
	}
	
	@Override
	public int hashCode() {
		return 11 ^ super.hashCode();
	}

	@Override
	public boolean isAnon(String objectUri) {
		return baseStore.isAnon(objectUri);
	}
}
