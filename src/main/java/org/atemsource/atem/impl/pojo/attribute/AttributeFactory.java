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
import java.util.HashSet;
import java.util.Set;

import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.attribute.AbstractAttribute;
import org.atemsource.atem.impl.infrastructure.BeanCreator;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AttributeFactory
{

	@Autowired
	protected BeanCreator beanCreator;

	public abstract boolean canCreate(PropertyDescriptor propertyDescriptor, EntityTypeCreationContext ctx);

	public abstract org.atemsource.atem.api.attribute.Attribute createAttribute(AbstractEntityType entityType,
		PropertyDescriptor propertyDescriptor, final EntityTypeCreationContext ctx);

	protected Set<EntityType> createValidtypesSet(final EntityType elementEntityType, final Class[] includedTypes,
		final Class[] excludedTypes, final EntityTypeCreationContext ctx)
	{
		Set<EntityType> validTypesSet;
		if (includedTypes == null || includedTypes.length == 0)
		{
			validTypesSet = elementEntityType.getSelfAndAllSubEntityTypes();
			if (excludedTypes != null)
			{
				for (Class type : excludedTypes)
				{
					validTypesSet.remove(ctx.getEntityTypeReference(type));
				}
			}
		}
		else
		{
			validTypesSet = new HashSet<EntityType>();
			for (Class type : includedTypes)
			{
				validTypesSet.add(ctx.getEntityTypeReference(type));
			}
		}
		return validTypesSet;
	}

	public abstract Collection<Class> getClasses();

	public abstract Class getCollectionClass();

	protected void setStandardProperties(final AbstractEntityType entityType,
		final PropertyDescriptor propertyDescriptor, final AbstractAttribute attribute)
	{
		String propertyName = propertyDescriptor.getName();
		attribute.setEntityType(entityType);
		attribute.setCode(propertyName);
		attribute.setWriteable(propertyDescriptor.isWritable());
	}
}
