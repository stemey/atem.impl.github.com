package org.atemsource.atem.impl.pojo.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.attribute.SingleAssociationAttribute;
import org.springframework.stereotype.Component;


@Component
public class SingleAttributeViewCreator implements ViewCreator
{

	@Inject
	private EntityTypeRepository entityTypeRepository;

	@Override
	public Serializable getKeyForParameter(Object[] parameter)
	{
		Object targetEntity = parameter[1];
		Attribute attribute = (Attribute) parameter[0];
		if (attribute instanceof SingleAssociationAttribute)
		{
			SingleAssociationAttribute<?> singleAttribute = (SingleAssociationAttribute) attribute;
			IdentityService service = singleAttribute.getTargetType().getService(IdentityService.class);
			if (service != null)
			{
				Serializable id = service.getId((EntityType<Object>)singleAttribute.getTargetType(), targetEntity);
				return new ArrayKey(new Serializable[]{id, attribute.getEntityType().getCode() + "." + attribute.getCode()});
			}

		}
		return null;
	}

	@Override
	public Serializable[] getKeys(Object entity)
	{
		EntityType<?> entityType = entityTypeRepository.getEntityType(entity);
		List<Serializable> keys = new ArrayList<Serializable>();
		for (Attribute attribute : entityType.getAttributes())
		{
			if (attribute instanceof SingleAssociationAttribute)
			{
				SingleAssociationAttribute<?> singleAttribute = (SingleAssociationAttribute) attribute;
				Object value = singleAttribute.getValue(entity);
				IdentityService service = singleAttribute.getTargetType().getService(IdentityService.class);
				if (service != null)
				{
					Serializable id = service.getId((EntityType<Object>)singleAttribute.getTargetType(), value);
					Serializable key = getKeyForParameter(new Object[]{attribute, value});
					if (key != null)
					{
						keys.add(key);
					}
				}

			}
		}
		return keys.toArray(new Serializable[0]);

	}

	@Override
	public Class<?>[] getParameterTypes()
	{
		return new Class[]{SingleAssociationAttribute.class, Object.class};
	}
}
