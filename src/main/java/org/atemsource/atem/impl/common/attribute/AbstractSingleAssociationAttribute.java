package org.atemsource.atem.impl.common.attribute;

import org.atemsource.atem.api.BeanLocator;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.AssociationAttribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractSingleAssociationAttribute<J> extends SingleAbstractAttribute<J> implements
	SingleAttribute<J>, AssociationAttribute<J, J>
{

	@Autowired
	private BeanLocator beanLocator;

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	public AbstractSingleAssociationAttribute()
	{
		super();
	}

	public J createEntity()
	{
		return getTargetType().createEntity();
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
	public EntityType<J> getTargetType(J entity)
	{
		if (entity == null)
		{
			return getTargetType();
		}
		else
		{
			EntityType type = entityTypeRepository.getEntityType(entity);
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