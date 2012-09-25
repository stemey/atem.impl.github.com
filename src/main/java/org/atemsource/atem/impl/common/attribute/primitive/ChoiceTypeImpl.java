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


import java.util.Map;

import org.atemsource.atem.api.type.primitive.ChoiceType;


public class ChoiceTypeImpl extends PrimitiveTypeImpl<Object> implements ChoiceType<Object>
{
	public static final String CHOICE_TYPE = "choice";

	private Map<String, Object> optionsMap;

	@Override
	public String getCode()
	{
		return CHOICE_TYPE;
	}

	@Override
	public Class<Object> getJavaType()
	{
		return Object.class;
	}

	@Override
	public Map<String, Object> getOptionsMap()
	{
		return optionsMap;
	}

	public void setOptionsMap(Map<String, Object> optionsMap)
	{
		this.optionsMap = optionsMap;
	}

}
