/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.common.attribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.MapAttribute;
import org.atemsource.atem.api.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class MapAttributeImpl<K, J> extends AbstractAttribute<J, Map> implements MapAttribute<K, J, Map>
{

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	private Type<K> keyType;

	private boolean sorted;

	@Override
	public void clear(Object entity)
	{
		Map map = getValue(entity);
		if (map != null)
		{
			map.clear();
		}
	}

	public void clearOrInitialize(Object entity)
	{
		Map map = getValue(entity);
		if (map == null)
		{
			setValue(entity, getEmptyMap());
		}
		else
		{
			map.clear();
		}
	}

	@Override
	public boolean containsValue(Object entity, J element)
	{
		Map map = getValue(entity);
		return map.containsValue(element);
	}

	@Override
	public Class<Map> getAssociationType()
	{
		return Map.class;
	}

	@Override
	public J getElement(Object entity, K keye)
	{
		return (J) getValue(entity).get(keye);
	}

	@Override
	public Map getEmptyMap()
	{
		if (isSorted())
		{
			return new TreeMap();
		}
		else
		{
			return new HashMap();
		}
	}

	@Override
	public Iterator<Entry<?, ?>> getIterator(Object entity)
	{
		return getValue(entity).entrySet().iterator();
	}

	@Override
	public Set<K> getKeySet(Object entity)
	{
		Map map = getValue(entity);
		return map.keySet();
	}

	@Override
	public Type<K> getKeyType()
	{
		return keyType;
	}

	@Override
	public Type<K> getKeyType(K key)
	{
		return entityTypeRepository.getType(key);
	}

	@Override
	public Class<Map> getReturnType()
	{
		return getAssociationType();
	}

	@Override
	public int getSize(Object entity)
	{
		Map value = getValue(entity);
		if (value == null)
		{
			return 0;
		}
		else
		{
			return value.size();
		}
	}

	@Override
	public Type<J> getTargetType(J value)
	{
		return entityTypeRepository.getEntityType(value);
	}

	@Override
	public Map getValue(Object entity)
	{
		return (Map) getAccessor().getValue(entity);
	}

	@Override
	public boolean isSorted()
	{
		return sorted;
	}

	@Override
	public void putElement(Object entity, K key, J value)
	{
		Map map = getValue(entity);
		map.put(key, value);
	}

	@Override
	public void removeKey(Object entity, K key)
	{
		Map map = getValue(entity);
		map.remove(key);

	}

	@Override
	public void removeValue(Object entity, J value)
	{
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void setKeyType(Type<K> keyType)
	{
		this.keyType = keyType;
	}

	public void setSorted(boolean sorted)
	{
		this.sorted = sorted;
	}

}
