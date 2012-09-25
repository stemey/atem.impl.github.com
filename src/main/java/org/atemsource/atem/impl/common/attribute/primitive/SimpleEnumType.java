/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.common.attribute.primitive;

import java.util.HashMap;
import java.util.Map;

import org.atemsource.atem.api.type.primitive.EnumType;


public class SimpleEnumType extends PrimitiveTypeImpl<Enum> implements EnumType
{
	private Class<Enum> enumClass;

	private Map<String, Enum> optionsMap;

	public SimpleEnumType(Class<Enum> enumClass)
	{
		super();
		this.enumClass = enumClass;
		optionsMap = new HashMap<String, Enum>();
		Enum<?>[] enumConstants = enumClass.getEnumConstants();
		if (enumConstants != null)
		{
			for (Enum<?> enumInstance : enumConstants)
			{
				optionsMap.put(enumInstance.name(), enumInstance);
			}
		}
		else
		{
			// Enum.calss itself
		}
	}

	@Override
	public Enum clone(Enum value)
	{
		return value;
	}

	public String getCode()
	{
		return enumClass.getName();
	}

	public Class<Enum> getJavaType()
	{
		return enumClass;
	}

	public Map<String, Enum> getOptionsMap()
	{
		return optionsMap;
	}

}
