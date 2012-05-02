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


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.atemsource.atem.api.attribute.primitive.DateType;


public class DateTypeImpl extends PrimitiveTypeImpl<Object> implements DateType
{

	private Precision precision;

	private Class dateClass;

	public DateTypeImpl(Precision precision, Class dateClass)
	{
		super();
		this.precision = precision;
		this.dateClass = dateClass;
	}

	@Override
	public Object clone(Object value)
	{
		return value;
	}

	@Override
	public String getCode()
	{
		return dateClass.getName();
	}

	@Override
	public Date getCurrentDate()
	{
		return Calendar.getInstance(Locale.GERMANY).getTime();
	}

	@Override
	public Class<Object> getJavaType()
	{
		return dateClass;
	}

	@Override
	public Precision getPrecision()
	{
		return precision;
	}
}
