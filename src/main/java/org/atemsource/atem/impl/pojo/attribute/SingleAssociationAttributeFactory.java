/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo.attribute;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.annotation.Association;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.attribute.SingleAssociationAttribute;
import org.atemsource.atem.impl.common.attribute.SingleAttributeImpl;
import org.atemsource.atem.impl.infrastructure.BeanCreator;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("pojo-SingleAssociationAttributeFactory")
public class SingleAssociationAttributeFactory extends AttributeFactory {

	@Autowired
	private BeanCreator beanCreator;

	@Override
	public boolean canCreate(PropertyDescriptor propertyDescriptor,
			EntityTypeCreationContext ctx) {
		if (propertyDescriptor.getPropertyType().isArray()) {
			return false;
		}

		EntityType entityType = ctx.getEntityTypeReference(propertyDescriptor
				.getPropertyType());
		return entityType == null || entityType instanceof EntityType<?>;
	}

	@Override
	public Attribute createAttribute(AbstractEntityType entityType,
			PropertyDescriptor propertyDescriptor, EntityTypeCreationContext ctx) {
		Association association = propertyDescriptor
				.getAnnotation(Association.class);
		NotNull notNull = propertyDescriptor.getAnnotation(NotNull.class);

		EntityType associatedEntityType = ctx
				.getEntityTypeReference(propertyDescriptor.getPropertyType());
		SingleAttributeImpl attribute = beanCreator
				.create(SingleAssociationAttribute.class);
		if (notNull != null) {
			attribute.setRequired(true);
		}
		if (association != null) {
			attribute.setTargetType(associatedEntityType);
		} else {
			attribute.setTargetType(ctx
					.getEntityTypeReference(propertyDescriptor
							.getPropertyType()));
		}
		
		
		initValidTypes(propertyDescriptor, ctx, attribute);

		attribute.setAccessor(propertyDescriptor.getAccessor());
		setStandardProperties(entityType, propertyDescriptor, attribute);
		return attribute;
	}

	@Override
	public Collection<Class> getClasses() {
		return null;
	}

	@Override
	public Class getCollectionClass() {
		return null;
	}

}
