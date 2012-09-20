/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo.attribute;

import java.util.Collection;
import java.util.Map;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.annotation.MapAssociation;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.attribute.MapAttributeImpl;
import org.atemsource.atem.impl.common.attribute.primitive.PrimitiveTypeFactory;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("PojoMapAssociationAttributeFactory")
public class MapAssociationAttributeFactory extends AttributeFactory
{
	@Autowired
	private PrimitiveTypeFactory primitiveTypeFactory;

	@Override
	public boolean canCreate(PropertyDescriptor propertyDescriptor, EntityTypeCreationContext ctx)
	{
		return Map.class.isAssignableFrom(propertyDescriptor.getPropertyType());
	}

	@Override
	public Attribute createAttribute(AbstractEntityType entityType, PropertyDescriptor propertyDescriptor,
		EntityTypeCreationContext ctx)
	{
		Class<?> propertyType = propertyDescriptor.getPropertyType();
		MapAttributeImpl attribute;

		MapAssociation association = propertyDescriptor.getAnnotation(MapAssociation.class);

		Type targeType = association == null ? null : ctx.getTypeReference(association.targetType());
		Type keyType = association == null ? null : ctx.getTypeReference(association.keyType());
		attribute = beanCreator.create(MapAttributeImpl.class);

		attribute.setTargetType(targeType);
		attribute.setKeyType(keyType);
		setStandardProperties(entityType, propertyDescriptor, attribute);
		attribute.setAccessor(propertyDescriptor.getAccessor());
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
		return Collection.class;
	}
}
