/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.json.attribute;

import org.atemsource.atem.api.infrastructure.util.ReflectionUtils;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.PrimitiveAttributeImpl;
import org.atemsource.atem.impl.common.attribute.primitive.PrimitiveTypeFactory;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class JsonAttribute<J> extends PrimitiveAttributeImpl<J>
{
	private Class javaType;

	@Autowired
	private PrimitiveTypeFactory primitiveTypeFactory;

	public JsonAttribute()
	{
		super();
		javaType = ReflectionUtils.getActualTypeParameter(getClass(), PrimitiveAttributeImpl.class);
	}

	@Override
	public Type<J> getTargetType()
	{
		return primitiveTypeFactory.getPrimitiveType(javaType);
	}

	@Override
	public Type<J> getTargetType(J value)
	{
		return getTargetType();
	}

	@Override
	public boolean isWriteable()
	{
		return true;
	}

}
