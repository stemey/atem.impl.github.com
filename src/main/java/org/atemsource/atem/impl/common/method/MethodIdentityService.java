package org.atemsource.atem.impl.common.method;

import java.io.Serializable;

import org.atemsource.atem.api.method.Method;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.primitive.SimpleTextType;


public class MethodIdentityService implements IdentityService
{
	private final Type<?> idType = new SimpleTextType();

	@Override
	public <E> Serializable getId(EntityType<E> entityType, E entity)
	{
		if (entity == null)
		{
			return null;
		}
		else
		{
			return ((Method) entity).getCode();
		}
	}

	@Override
	public Type<?> getIdType(EntityType<?> entityType)
	{
		return idType;
	}

}
