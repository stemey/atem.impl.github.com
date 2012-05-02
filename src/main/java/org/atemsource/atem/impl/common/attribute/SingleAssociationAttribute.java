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
package org.atemsource.atem.impl.common.attribute;


import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.AssociationAttribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.infrastructure.BeanLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class SingleAssociationAttribute<J> extends SingleAttributeImpl<J> implements SingleAttribute<J>,
	AssociationAttribute<J, J>
{

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	@Autowired
	private BeanLocator beanLocator;

	public J createEntity()
	{
		return (J) ((EntityType<J>) getTargetType()).createEntity();
	}

	public J createEntity(String typeCode)
	{
		return (J) entityTypeRepository.getEntityType(typeCode).createEntity();
	}

	@Override
	public EntityType<J> getTargetType()
	{
		return (EntityType<J>) super.getTargetType();
	}

	@Override
	public EntityType<J> getTargetType(Object entity)
	{
		if (entity == null)
		{
			return getTargetType();
		}
		else
		{
			EntityType type = (EntityType<J>) entityTypeRepository.getEntityType(entity);
			if (type == null)
			{
				return getTargetType();
			}
			else
			{
				return type;
			}
		}
	}

	@Override
	public J getValue(Object entity)
	{
		return (J) getAccessor().getValue(entity);
	}

	@Override
	public boolean isEqual(Object entity, Object other)
	{
		J valueA = getValue(entity);
		J valueB = getValue(other);
		if (valueA == null && valueB == null)
		{
			return true;
		}
		else if (valueA == null || valueB == null)
		{
			return false;
		}
		else
		{

			if (!getTargetType(valueA).equals(getTargetType(valueB)))
			{
				return false;
			}
			else
			{
				return getTargetType(valueA).isEqual(valueA, valueB);
			}

		}
	}

}
