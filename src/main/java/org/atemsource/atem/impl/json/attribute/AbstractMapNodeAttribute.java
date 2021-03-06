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
import org.atemsource.atem.impl.json.JsonUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractMapNodeAttribute<V> extends AbstractAttribute<V, ObjectNode> implements
	MapAttribute<String, V, ObjectNode>
{

	@Autowired
	protected EntityTypeRepository entityTypeRepository;

	private ObjectMapper objectMapper;

	public AbstractMapNodeAttribute()
	{
		super();
	}

	@Override
	public void clear(Object entity)
	{
		getValue(entity).removeAll();
	}

	@Override
	public boolean containsKey(Object entity, String key)
	{
		if (entity == null)
		{
			return false;
		}
		else
		{
			ObjectNode node = (ObjectNode) entity;
			return node.get(key) != null;
		}
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

	public CollectionSortType getCollectionSortType()
	{
		return CollectionSortType.NONE;
	}

	@Override
	public V getElement(Object entity, String keye)
	{

		return (V) JsonUtils.convertToJava(getValue(entity).get(keye));
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
		return untypedMap.entrySet().iterator();
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

	public ObjectMapper getObjectMapper()
	{
		return objectMapper;
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
	public boolean isSorted()
	{
		return false;
	}

	@Override
	public boolean isWriteable()
	{
		return true;
	}

	@Override
	public void putElement(Object entity, String key, V value)
	{
		getValue(entity).put(key, JsonUtils.convertToJson(value));
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

	public void setObjectMapper(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	@Override
	public void setValue(Object entity, ObjectNode value)
	{
		ObjectNode node = (ObjectNode) entity;
		if (node.isNull())
		{
			throw new NullPointerException("entity is null");
		}
		else if (value == null)
		{
			node.putNull(getCode());
		}
		else
		{
			node.put(getCode(), value);
		}
	}

}