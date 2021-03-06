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


public class SimplePrimitiveType extends PrimitiveTypeImpl implements PrimitiveType
{

	private Class clazz;

	public SimplePrimitiveType(Class clazz, boolean nullable)
	{
		super();
		this.clazz = clazz;
		setNullable(nullable);
	}

	@Override
	public Object deserialize(Serializable value)
	{
		return value;
	}

	@Override
	public String getCode()
	{
		return clazz.getSimpleName();
	}

	@Override
	public Class getJavaType()
	{
		return clazz;
	}

	@Override
	public Serializable serialize(Object value)
	{
		return (Serializable) value;
	}

}
