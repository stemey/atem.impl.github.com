/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.json.attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.OrderableCollection;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.AbstractAttribute;
import org.atemsource.atem.impl.json.JsonUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class ArrayNodeAttribute extends AbstractAttribute<Object, ArrayNode> implements
	CollectionAttribute<Object, ArrayNode>, OrderableCollection<Object, ArrayNode>
{

	private ObjectMapper objectMapper;

	@Override
	public void addElement(Object entity, int index, Object value)
	{
		ArrayNode array = getValue(entity);
		array.set(index, JsonUtils.convertToJson(value));
	}

	public void addElement(Object entity, Object element)
	{
		ArrayNode arrayNode = getArrayNode(entity);
		arrayNode.add(JsonUtils.convertToJson(element));
	}

	public void clear(Object entity)
	{
		ArrayNode arrayNode = getArrayNode(entity);
		arrayNode.removeAll();
	}

	public boolean contains(Object entity, Object element)
	{
		throw new UnsupportedOperationException("notimplemented yet");
	}

	private ArrayNode getArrayNode(Object entity)
	{
		return (ArrayNode) ((ObjectNode) entity).get(getCode());
	}

	public Class<ArrayNode> getAssociationType()
	{
		return ArrayNode.class;
	}

	public CollectionSortType getCollectionSortType()
	{
		return CollectionSortType.ORDERABLE;
	}

	@Override
	public Object getElement(Object entity, int index)
	{
		return JsonUtils.convertToJava(getValue(entity).get(index));
	}

	public Collection<Object> getElements(Object entity)
	{
		List<Object> collection = new ArrayList<Object>();
		ArrayNode arrayNode = getArrayNode(entity);
		for (int index = 0; index < arrayNode.size(); index++)
		{
			collection.add(JsonUtils.convertToJson(arrayNode.get(index)));
		}
		return collection;
	}

	public ArrayNode getEmptyCollection(Object entity)
	{
		return objectMapper.createArrayNode();
	}

	@Override
	public int getIndex(Object entity, Object value)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public Iterator<Object> getIterator(Object entity)
	{
		return getElements(entity).iterator();
	}

	public ObjectMapper getObjectMapper()
	{
		return objectMapper;
	}

	@Override
	public Class<ArrayNode> getReturnType()
	{
		return getAssociationType();
	}

	public Serializable getSerializableValue(Object entity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getSize(Object entity)
	{
		return getArrayNode(entity).size();
	}

	public Type<Object> getTargetType(Object value)
	{
		return getTargetType();
	}

	public String getTypeCode()
	{
		return "arrayNode";
	}

	public ArrayNode getValue(Object entity)
	{
		return getArrayNode(entity);
	}

	@Override
	public boolean isEqual(Object entity, Object other)
	{
		ArrayNode valueA = getValue(entity);
		ArrayNode valueB = getValue(other);
		if (valueA == null && valueB == null)
		{
			return true;
		}
		else if (valueA != null && valueB == null)
		{
			return false;
		}
		else if (valueA == null && valueB != null)
		{
			return false;
		}
		else if (valueA.size() != valueB.size())
		{
			return false;
		}
		else
		{
			for (int index = 0; index < valueA.size(); index++)
			{
				ObjectNode a = (ObjectNode) getElement(entity, index);
				ObjectNode b = (ObjectNode) getElement(other, index);
				if (!getTargetType().isEqual(a, b))
				{
					return false;
				}

			}
		}
		return true;

	}

	@Override
	public boolean isWriteable()
	{
		return true;
	}

	@Override
	public void moveElement(Object entity, int fromIndex, int toIndex)
	{
		ArrayNode value = getValue(entity);
		ObjectNode element = (ObjectNode) value.remove(fromIndex);
		value.set(toIndex, element);

	}

	@Override
	public Object removeElement(Object entity, int index)
	{
		return getValue(entity).remove(index);
	}

	public void removeElement(Object entity, Object element)
	{
		throw new UnsupportedOperationException("notimplemented yet");
	}

	public void setObjectMapper(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	@Override
	public void setValue(Object entity, ArrayNode value)
	{
		ObjectNode node = (ObjectNode) entity;
		if (value == null)
		{
			node.putNull(getCode());
		}
		else
		{
			node.put(getCode(), value);
		}
	}
}
