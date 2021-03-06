/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.common.attribute.primitive;

public class IntegerType extends PrimitiveTypeImpl<Integer> implements
	org.atemsource.atem.api.type.primitive.IntegerType
{

	public static final String TYPE_CODE = "Integer";

	public IntegerType(boolean nullable) {
		super(nullable);
	}

	public IntegerType() {
	}

	@Override
	public String getCode()
	{
		return TYPE_CODE;
	}

	@Override
	public boolean isInstance(Object value)
	{
		return int.class.isInstance(value) ||  Integer.class.isInstance(value);
	}
	@Override
	public Class<Integer> getJavaType()
	{
		return isNullable() ? Integer.class : int.class;
	}

}
