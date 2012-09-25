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

import java.util.Locale;


public class DoubleType extends PrimitiveTypeImpl<Double> implements
	org.atemsource.atem.api.type.primitive.DoubleType
{
	public static final String TYPE_CODE = "Double";

	private Integer fractionDigits;

	public String getCode()
	{
		return TYPE_CODE;
	}

	public Integer getFractionDigits()
	{
		return fractionDigits;
	}

	public Class<Double> getJavaType()
	{
		return isNullable() ? Double.class : double.class;
	}

	public String getLabel(Double entity, Locale locale)
	{
		if (entity == null)
		{
			return "undefined";
		}
		else
		{
			return String.valueOf(entity);
		}
	}

	public void setFractionDigits(Integer fractionDigits)
	{
		this.fractionDigits = fractionDigits;
	}

}
