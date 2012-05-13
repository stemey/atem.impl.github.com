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
package org.atemsource.atem.impl.pojo.attribute;


import java.util.Collection;

import javax.annotation.Resource;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.attribute.PrimitiveAttributeImpl;
import org.atemsource.atem.impl.common.attribute.primitive.PrimitiveTypeFactory;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.springframework.stereotype.Component;


@Component("pojo-PrimitiveAttributeFactory")
public class PrimitiveAttributeFactory extends AttributeFactory
{

	@Resource
	private PrimitiveTypeFactory primitiveTypeFactory;

	@Override
	public boolean canCreate(PropertyDescriptor propertyDescriptor, EntityTypeCreationContext ctx)
	{
		return primitiveTypeFactory.getPrimitiveType(propertyDescriptor.getPropertyType()) != null;
	}

	@Override
	public Attribute createAttribute(AbstractEntityType entityType, PropertyDescriptor propertyDescriptor,
		EntityTypeCreationContext ctx)
	{
		PrimitiveAttributeImpl attribute = new PrimitiveAttributeImpl();
		attribute.setAccessor(new PojoAccessor(propertyDescriptor.getField(), propertyDescriptor.getReadMethod(),
			propertyDescriptor.getWriteMethod(), propertyDescriptor.isFieldAccess()));
		setStandardProperties(entityType, propertyDescriptor, attribute);
		attribute.setTargetType(primitiveTypeFactory.getPrimitiveType(propertyDescriptor.getPropertyType()));
		return attribute;
	}

	@Override
	public Collection<Class> getClasses()
	{
		return primitiveTypeFactory.getClasses();
	}

	@Override
	public Class getCollectionClass()
	{
		return null;
	}

}
