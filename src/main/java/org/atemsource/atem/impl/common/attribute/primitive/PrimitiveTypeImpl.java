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
package org.atemsource.atem.impl.common.attribute.primitive;


import java.io.Serializable;

import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.api.type.Type;


public abstract class PrimitiveTypeImpl<J> implements PrimitiveType<J>
{

	

	@Override
	public boolean isInstance(Object value) {
		return getJavaType().isInstance(value);
	}

	@Override
	public boolean equals(Object obj) {
		return ((PrimitiveType<?>)obj).getJavaType().equals(getJavaType());
	}

	@Override
	public int hashCode() {
		return getJavaType().hashCode();
	}

	private boolean nullable = true;

	public PrimitiveTypeImpl()
	{
		super();
	}

	@Override
	public J clone(J value)
	{
		if (value == null)
		{
			return value;
		}
		if (value instanceof Number)
		{
			return value;
		}
		else if (value instanceof String)
		{
			return (J) new String((String) value);
		}
		else if (getJavaType().isPrimitive())
		{
			return value;
		}
		else
		{
			throw new UnsupportedOperationException("implement method");
		}
	}

	@Override
	public J deserialize(Serializable value)
	{
		return (J) value;
	}

	@Override
	public boolean isAssignableFrom(Type<?> source)
	{
		return getJavaType().isAssignableFrom(source.getJavaType());
	}

	@Override
	public boolean isEqual(J a, J b)
	{
		if (a == null && b == null)
		{
			return true;
		}
		else if (a == null && b != null || a != null && b == null)
		{
			return false;
		}
		else
		{
			return a.equals(b);
		}
	}

	@Override
	public boolean isNullable()
	{
		return nullable;
	}

	@Override
	public Serializable serialize(J value)
	{
		return (Serializable) value;
	}

	public void setNullable(boolean nullable)
	{
		this.nullable = nullable;
	}

}
