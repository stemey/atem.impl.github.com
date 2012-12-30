/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.json.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.MapAttribute;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.attribute.relation.ArrayAssociationAttribute;
import org.atemsource.atem.api.attribute.relation.ListAssociationAttribute;
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
public class ChildrenAttribute implements ListAssociationAttribute< Object> {

	@PostConstruct
	public void initialize() {
		targetType=entityTypeRepository.getType(Object.class);
	}
	
	@Inject
	private EntityTypeRepository entityTypeRepository;

	@Override
	public Type<Object> getTargetType(Object value) {
		// TODO we dont check the property type here.
		if (value instanceof ObjectNode) {
			return entityTypeRepository.getEntityType(ObjectNode.class.getName());
		} else if (value instanceof ArrayNode) {
			return entityTypeRepository.getEntityType(ArrayNode.class.getName());
		} else {
			return entityTypeRepository.getType(value);
		}
	}

	@Override
	public List getValue(Object entity) {
		List< Object> children = new ArrayList< Object>();
		ArrayNode node = (ArrayNode) entity;
		for (int index=0;index<node.size();index++){
			JsonNode child = node.get(index);
			children.add( JsonUtils.convertToJava(child));

		}
		return children;
	}

	@Override
	public void setValue(Object entity, List value) {
		ArrayNode oldNode = (ArrayNode) entity;
		oldNode.removeAll();
		for (int index=0;index<oldNode.size();index++){
			JsonNode child = oldNode.get(index);
			oldNode.add( JsonUtils.convertToJson(child));

		}
	}

	@Override
	public Class<List> getAssociationType() {
		return List.class;
	}
	@Override
	public Type<Object>[] getValidTargetTypes() {
		return null;
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
	public Class<List> getReturnType() {
		return List.class;
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
	public int getSize(Object entity) {
		return ((ArrayNode)entity).size();
	}

	@Override
	public void addElement(Object entity, Object element) {
		ArrayNode node=(ArrayNode) entity;
		node.add(JsonUtils.convertToJson(element));
	}

	@Override
	public boolean contains(Object entity, Object element) {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public Collection<Object> getElements(Object entity) {
		List<Object> list = new ArrayList<Object>();
		ArrayNode node = (ArrayNode) entity;
		for (int index=0;index<node.size();index++) {
			list.add(JsonUtils.convertToJson(node.get(index)));
		}
		return list;
	}

	@Override
	public List getEmptyCollection(Object entity) {
		return new ArrayList();
	}

	@Override
	public Iterator<Object> getIterator(Object entity) {
		return getElements(entity).iterator();
	}

	@Override
	public void removeElement(Object entity, Object element) {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public void addElement(Object entity, int index, Object value) {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public Object getElement(Object entity, int index) {
		ArrayNode node=(ArrayNode) entity;
		return JsonUtils.convertToJava(node.get(index));
	}

	@Override
	public int getIndex(Object entity, Object value) {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public void moveElement(Object entity, int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public Object removeElement(Object entity, int index) {
		ArrayNode node=(ArrayNode) entity;
		return JsonUtils.convertToJava(node.remove(index));
	}

	@Override
	public boolean isDerived() {
		// TODO Auto-generated method stub
		return false;
	}



}
