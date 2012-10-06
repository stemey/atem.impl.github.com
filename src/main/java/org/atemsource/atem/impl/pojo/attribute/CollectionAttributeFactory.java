/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo.attribute;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.annotation.Association;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.attribute.collection.AbstractCollectionAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.CollectionAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.ListAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.SetAttributeImpl;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.springframework.stereotype.Component;


@Component("Pojo-MultiAssociationAttributeFactory")
public class CollectionAttributeFactory extends AttributeFactory
{

	@Override
	public boolean canCreate(PropertyDescriptor propertyDescriptor, EntityTypeCreationContext ctx)
	{
		return Collection.class.isAssignableFrom(propertyDescriptor.getPropertyType());
	}

	@Override
	public Attribute createAttribute(AbstractEntityType entityType, PropertyDescriptor propertyDescriptor,
		EntityTypeCreationContext ctx)
	{
		Class<?> propertyType = propertyDescriptor.getPropertyType();
		AbstractCollectionAttributeImpl attribute;

		Association association = propertyDescriptor.getAnnotation(Association.class);
		Class[] includedTypes = null;
		Class[] excludedTypes = null;
		Type targetType;
		if (association != null)
		{
			targetType = ctx.getTypeReference(association.targetType());
			if (targetType == null)
			{
				throw new IllegalStateException("cannot find type reference for " + association.targetType());
			}
		}
		else
		{
			targetType = null;
		}
		Type targeType = association == null ? null : ctx.getTypeReference(association.targetType());

		if (List.class.isAssignableFrom(propertyType))
		{
			attribute = beanCreator.create(ListAttributeImpl.class);
			attribute.setCollectionSortType(CollectionSortType.ORDERABLE);
		}
		else if (Set.class.isAssignableFrom(propertyType))
		{
			attribute = beanCreator.create(SetAttributeImpl.class);
			if (SortedSet.class.isAssignableFrom(propertyType))
			{
				attribute.setCollectionSortType(CollectionSortType.ORDERABLE);

			}
			else
			{
				attribute.setCollectionSortType(CollectionSortType.NONE);
			}
		}
		else if (Collection.class.isAssignableFrom(propertyType))
		{
			attribute = beanCreator.create(CollectionAttributeImpl.class);
			attribute.setCollectionSortType(CollectionSortType.NONE);
		}
		else
		{
			throw new IllegalStateException("properyType " + propertyType.getName()
				+ " cannot be used for multi associations");
		}
		if (targeType != null && targeType instanceof EntityType)
		{
			if (association.composition())
			{
				attribute.setComposition(true);
			}

			// there are two sub classes
			// else if (Collection.class.isAssignableFrom(propertyType))
			// {
			// attribute = beanCreator.create(CollectionAssociationAttributeImpl.class);
			// }

			targeType = ctx.getEntityTypeReference(association.targetType());
			attribute.setTargetType(targeType);
			setStandardProperties(entityType, propertyDescriptor, attribute);
			attribute.setAccessor(propertyDescriptor.getAccessor());
		}
		else
		{

			attribute.setTargetType(targeType);
			setStandardProperties(entityType, propertyDescriptor, attribute);
			attribute.setAccessor(propertyDescriptor.getAccessor());
		}
		initValidTypes(propertyDescriptor, ctx, attribute);

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
