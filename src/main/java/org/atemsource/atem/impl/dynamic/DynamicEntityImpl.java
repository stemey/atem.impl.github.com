/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.dynamic;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DynamicEntityImpl implements DynamicEntity
{

	private final Map<String, Object> attributes = new HashMap<String, Object>();

	public void clear() {
		attributes.clear();
	}

	public boolean containsKey(Object key) {
		return attributes.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return attributes.containsValue(value);
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return attributes.entrySet();
	}

	public boolean equals(Object o) {
		return attributes.equals(o);
	}

	public int hashCode() {
		return attributes.hashCode();
	}

	public boolean isEmpty() {
		return attributes.isEmpty();
	}

	public Set<String> keySet() {
		return attributes.keySet();
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		attributes.putAll(m);
	}

	public int size() {
		return attributes.size();
	}

	public Collection<Object> values() {
		return attributes.values();
	}

	private Serializable id;

	private String typeCode;

	public DynamicEntityImpl()
	{
		super();
	}

	@Override
	public Object get(Object key)
	{
		return attributes.get(key);
	}

	public String getAsString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(typeCode);
		builder.append("\n");
		for (String attribute : getAttributeNames())
		{
			builder.append(attribute);
			builder.append(" : ");

			Object object = get(attribute);
			builder.append(String.valueOf(object));
			builder.append("\n");
		}
		return builder.toString();
	}

	@Override
	public String[] getAttributeNames()
	{
		return attributes.keySet().toArray(new String[0]);
	}

	public Serializable getId()
	{
		return id;
	}

	@Override
	public String getTypeCode()
	{
		return typeCode;
	}

	@Override
	public Object put(String key, Object value)
	{
		return attributes.put(key, value);
	}

	@Override
	public Object remove(Object key)
	{
		return attributes.remove(key);
	}

	public void setId(Serializable id)
	{
		this.id = id;
	}

	public void setTypeCode(String typeCode)
	{
		this.typeCode = typeCode;
	}

	@Override
	public String toString()
	{
		return getAsString();
	}

}
