/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo.attribute;

import java.lang.reflect.Array;
import java.util.Collection;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.annotation.Association;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.attribute.AbstractAttribute;
import org.atemsource.atem.impl.common.attribute.collection.ArrayAttributeImpl;
import org.atemsource.atem.impl.common.attribute.primitive.PrimitiveTypeFactory;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("Pojo-ArrayAttributeFactory")
public class ArrayAttributeFactory extends AttributeFactory
{

	@Autowired
	private PrimitiveTypeFactory primitiveTypeFactory;

	@Override
	public boolean canCreate(PropertyDescriptor propertyDescriptor, EntityTypeCreationContext ctx)
	{
		return propertyDescriptor.getPropertyType().isArray();
	}

	@Override
	public Attribute createAttribute(AbstractEntityType entityType, PropertyDescriptor propertyDescriptor,
		EntityTypeCreationContext ctx)
	{
		AbstractAttribute attribute;

		Association association = propertyDescriptor.getAnnotation(Association.class);
		Class[] includedTypes = null;
		Class[] excludedTypes = null;
		Class targetClass = propertyDescriptor.getPropertyType().getComponentType();
		Type targetType = primitiveTypeFactory.getPrimitiveType(targetClass);
		if (targetType == null)
		{

			targetType = ctx.getEntityTypeReference(targetClass);
		}
		attribute = beanCreator.create(ArrayAttributeImpl.class);
		((ArrayAttributeImpl) attribute).setTargetType(targetType);
		setStandardProperties(entityType, propertyDescriptor, attribute);
		((ArrayAttributeImpl) attribute).setAccessor(propertyDescriptor.getAccessor());

		return attribute;

	}

	@Override
	public Collection<Class> getClasses()
	{
		return null;
	}

	@Override
	public Class getCollectionClass()
	{
		return Array.class;
	}
}
