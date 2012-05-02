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
package org.atemsource.atem.impl.dynamic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class DynamicEntityImpl implements DynamicEntity
{

	private Map<String, Object> attributes = new HashMap<String, Object>();

	private String typeCode;

	private Serializable id;

	public DynamicEntityImpl()
	{
		super();
	}

	public Object get(Object key)
	{
		return attributes.get(key);
	}

	public Serializable getId()
	{
		return id;
	}

	public String getTypeCode()
	{
		return typeCode;
	}

	public Object put(String key, Object value)
	{
		return attributes.put(key, value);
	}

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

}
