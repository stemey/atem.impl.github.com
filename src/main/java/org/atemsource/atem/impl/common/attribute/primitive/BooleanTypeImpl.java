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

import org.atemsource.atem.api.type.primitive.BooleanType;


public class BooleanTypeImpl extends PrimitiveTypeImpl<Boolean> implements BooleanType
{

	public static final String TYPE_CODE = "boolean";

	@Override
	public String getCode()
	{
		return TYPE_CODE;
	}

	@Override
	public Class<Boolean> getJavaType()
	{
		return isNullable() ? Boolean.class : boolean.class;
	}


	@Override
	public boolean isInstance(Object value)
	{
		return boolean.class.isInstance(value) ||  Boolean.class.isInstance(value);
	}
	
	public BooleanTypeImpl() {
		super();
	}

	public BooleanTypeImpl(boolean nullable) {
		super(nullable);
	}
}
