/*******************************************************************************
 * Stefan Meyer, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.json.attribute;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.MapAttribute;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.AbstractAttribute;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class MapNodeAttribute<V extends ObjectNode> extends AbstractAttribute<V, ObjectNode> implements
	MapAttribute<String, V, ObjectNode>
{
	@Autowired
	private EntityTypeRepository entityTypeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void clear(Object entity)
	{
		getValue(entity).removeAll();
	}

	@Override
	public boolean containsValue(Object entity, V element)
	{
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public Class<ObjectNode> getAssociationType()
	{
		return ObjectNode.class;
	}

	@Override
	public CollectionSortType getCollectionSortType()
	{
		return CollectionSortType.NONE;
	}

	@Override
	public V getElement(Object entity, String keye)
	{

		return (V) getValue(entity).get(keye);
	}

	@Override
	public ObjectNode getEmptyMap()
	{
		return objectMapper.createObjectNode();
	}

	@Override
	public Iterator<Entry<?, ?>> getIterator(Object entity)
	{
		Map<String, V> elements = new HashMap<String, V>();
		Iterator<Map.Entry<String, JsonNode>> fields = getValue(entity).getFields();
		for (; fields.hasNext();)
		{
			Entry<String, JsonNode> next = fields.next();
			elements.put(next.getKey(), (V) next.getValue());
		}

		Map untypedMap = elements;
		return (Iterator<Entry<?, ?>>) untypedMap.entrySet().iterator();
	}

	@Override
	public Set<String> getKeySet(Object entity)
	{
		ObjectNode node = (ObjectNode) entity;
		Set<String> fields = new HashSet<String>();
		for (Iterator<String> i = node.getFieldNames(); i.hasNext();)
		{
			fields.add(i.next());
		}
		return fields;
	}

	@Override
	public Type<String> getKeyType()
	{
		return entityTypeRepository.getType(String.class);
	}

	@Override
	public Type<String> getKeyType(String key)
	{
		return entityTypeRepository.getType(String.class);
	}

	@Override
	public Class<ObjectNode> getReturnType()
	{
		return getAssociationType();
	}

	@Override
	public int getSize(Object entity)
	{
		return getValue(entity).size();
	}

	@Override
	public Type<V> getTargetType(V value)
	{
		return entityTypeRepository.getType(value);
	}

	@Override
	public ObjectNode getValue(Object entity)
	{
		ObjectNode node = (ObjectNode) entity;
		if (node.isNull())
		{
			throw new NullPointerException("entity is null");
		}
		else
		{
			return (ObjectNode) node.get(getCode());
		}
	}

	@Override
	public void putElement(Object entity, String key, V value)
	{
		getValue(entity).put(key, value);
	}

	@Override
	public void removeKey(Object entity, String key)
	{
		getValue(entity).remove(key);
	}

	@Override
	public void removeValue(Object entity, V value)
	{
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void setValue(Object entity, ObjectNode value)
	{
		ObjectNode node = (ObjectNode) entity;
		if (node.isNull())
		{
			throw new NullPointerException("entity is null");
		}
		else
		{
			node.put(getCode(), value);
		}
	}

}
