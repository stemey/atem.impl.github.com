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

import org.atemsource.atem.api.type.TypedId;


public class DynamicTypedId implements TypedId
{
	private Serializable id;

	private String typeCode;

	public DynamicTypedId(Serializable id, String typeCode)
	{
		super();
		this.id = id;
		this.typeCode = typeCode;
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
		DynamicTypedId other = (DynamicTypedId) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (typeCode == null)
		{
			if (other.typeCode != null)
				return false;
		}
		else if (!typeCode.equals(other.typeCode))
			return false;
		return true;
	}

	public Class getEntityClass()
	{
		return DynamicEntity.class;
	}

	public Serializable getEntityId()
	{
		return id;
	}

	public String getIdAsString()
	{
		return String.valueOf(id);
	}

	public Object getPrimaryKey()
	{
		return id;
	}

	public String getTypeCode()
	{
		return typeCode;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((typeCode == null) ? 0 : typeCode.hashCode());
		return result;
	}

	public boolean isPersistent()
	{
		return id != null;
	}

	public void setId(String id)
	{
		this.id = id;
	}

}
