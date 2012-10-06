/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.json.attribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.MapAttribute;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.json.JsonUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PropertiesAttribute implements MapAttribute<String, Object, Map> {

	@PostConstruct
	public void initialize() {
		keyType=entityTypeRepository.getType(String.class);
		targetType=entityTypeRepository.getType(Object.class);
	}
	
	@Inject
	private EntityTypeRepository entityTypeRepository;

	@Override
	public Type<Object> getTargetType(Object value) {
		if (value instanceof ObjectNode) {
			return entityTypeRepository.getEntityType(ObjectNode.class.getName());
		} else if (value instanceof ArrayNode) {
			return entityTypeRepository.getEntityType(ArrayNode.class.getName());
		} else {
			return entityTypeRepository.getType(value);
		}
	}

	@Override
	public Map getValue(Object entity) {
		Map<String, Object> children = new HashMap<String, Object>();
		ObjectNode node = (ObjectNode) entity;
		Iterator<String> fieldNames = node.getFieldNames();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			JsonNode child = node.get(fieldName);
			children.put(fieldName, JsonUtils.convertToJava(child));

		}
		return children;
	}

	@Override
	public void setValue(Object entity, Map value) {
		ObjectNode oldNode = (ObjectNode) entity;
		oldNode.removeAll();
		Iterator<String> fieldNames = value.keySet().iterator();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			oldNode.put(fieldName,
					JsonUtils.convertToJson(value.get(fieldName)));
		}
	}

	@Override
	public Class<Map> getAssociationType() {
		return Map.class;
	}

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private EntityType entityType;

	@Override
	public Class<Map> getReturnType() {
		return Map.class;
	}

	@Override
	public Cardinality getTargetCardinality() {
		return Cardinality.ONE;
	}

	private Type targetType;

	@Override
	public boolean isComposition() {
		return false;
	}

	@Override
	public boolean isEqual(Object entity, Object other) {
		return entity.equals(other);
	}

	private boolean required;

	@Override
	public boolean isWriteable() {
		return true;
	}

	@Override
	public void clear(Object entity) {
		((ObjectNode)entity).removeAll();
	}

	@Override
	public boolean containsValue(Object entity, Object element) {
		throw new UnsupportedOperationException("not supported");
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Type getTargetType() {
		return targetType;
	}

	@Override
	public CollectionSortType getCollectionSortType() {
		return CollectionSortType.NONE;
	}

	@Override
	public Object getElement(Object entity, String keye) {
		return getValue(entity).get(keye);
	}

	@Override
	public Map getEmptyMap() {
		return new HashMap<String,Object>();
	}

	@Override
	public Iterator<Entry<?, ?>> getIterator(Object entity) {
		return getValue(entity).entrySet().iterator();
	}

	@Override
	public Set<String> getKeySet(Object entity) {
		return getValue(entity).keySet();
	}

	@Override
	public Type<String> getKeyType() {
		return keyType;
	}

	private Type<String> keyType;

	@Override
	public int getSize(Object entity) {
		return ((ObjectNode)entity).size();
	}

	@Override
	public void putElement(Object entity, String key, Object value) {
		((ObjectNode)entity).put(key, JsonUtils.convertToJson(value));
	}

	@Override
	public void removeKey(Object entity, String key) {
		((ObjectNode)entity).remove(key);
	}

	@Override
	public void removeValue(Object entity, Object value) {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public Type<String> getKeyType(String key) {
		return keyType;
	}

	@Override
	public Type<Object>[] getValidTargetTypes() {
		return null;
	}

}
