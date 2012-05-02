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
package org.atemsource.atem.impl.pojo;


import java.io.Serializable;

import org.atemsource.atem.api.type.TypedId;


public class SerializableTypeId implements TypedId
{

	private Class clazz;

	private Serializable serializable;

	public SerializableTypeId(Serializable serializable, Class clazz)
	{
		super();
		this.serializable = serializable;
		this.clazz = clazz;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerializableTypeId other = (SerializableTypeId) obj;
		if (clazz == null)
		{
			if (other.clazz != null)
				return false;
		}
		else if (!clazz.equals(other.clazz))
			return false;
		if (serializable == null)
		{
			if (other.serializable != null)
				return false;
		}
		else if (!serializable.equals(other.serializable))
			return false;
		return true;
	}

	@Override
	public Class getEntityClass()
	{
		return clazz;
	}

	@Override
	public Serializable getEntityId()
	{
		return serializable;
	}

	@Override
	public String getIdAsString()
	{
		return String.valueOf(serializable);
	}

	@Override
	public Object getPrimaryKey()
	{
		return serializable;
	}

	@Override
	public String getTypeCode()
	{
		return clazz.getName();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((serializable == null) ? 0 : serializable.hashCode());
		return result;
	}

}
