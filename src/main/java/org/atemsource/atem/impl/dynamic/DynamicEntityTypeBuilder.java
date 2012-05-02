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


import org.atemsource.atem.api.attribute.primitive.PrimitiveType;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.impl.common.AbstractEntityTypeBuilder;
import org.atemsource.atem.impl.common.attribute.PrimitiveAttributeImpl;
import org.atemsource.atem.impl.common.attribute.SingleAttributeImpl;
import org.atemsource.atem.impl.dynamic.attribute.DynamicAccessor;
import org.atemsource.atem.impl.dynamic.attribute.SerializablePrimitiveAttributeImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Scope("prototype")
@Component
public class DynamicEntityTypeBuilder extends AbstractEntityTypeBuilder
{

	private boolean serializingPrimitives;

	public <J> SingleAttribute<J> addPrimitiveAttribute(String code, PrimitiveType<J> type)
	{
		SingleAttributeImpl<J> attribute;
		if (serializingPrimitives)
		{
			attribute = new SerializablePrimitiveAttributeImpl<J>();
		}
		else
		{
			attribute = new PrimitiveAttributeImpl<J>();

		}
		attribute.setAccessor(new DynamicAccessor(code));
		attribute.setWriteable(true);
		attribute.setTargetType(type);
		attribute.setCode(code);
		addAttribute(attribute);
		attribute.setEntityType(getEntityType());
		return attribute;
	}

	public void setSerializingPrimitives(boolean serializingPrimitives)
	{
		this.serializingPrimitives = serializingPrimitives;
	}

}
