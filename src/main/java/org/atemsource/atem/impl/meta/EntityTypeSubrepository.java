package org.atemsource.atem.impl.meta;

import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.view.View;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.pojo.ScannedPojoEntityTypeRepository;
import org.atemsource.atem.spi.EntityTypeCreationContext;


public class EntityTypeSubrepository extends ScannedPojoEntityTypeRepository
{

	@Override
	public EntityType getEntityType(Class clazz)
	{
		if (!Type.class.isAssignableFrom(clazz))
		{
			return null;
		}
		EntityType entityType = super.getEntityType(clazz);
		if (entityType == null)
		{
			entityType = getFromInterfaces(clazz);
			classToEntityTypes.put(clazz, (AbstractEntityType<Object>) entityType);
			nameToEntityTypes.put(clazz.getName(), (AbstractEntityType<Object>) entityType);
		}
		return entityType;
	}

	@Override
	public EntityType<Object> getEntityType(Object entity)
	{
		return getEntityType(entity.getClass());
	}

	protected EntityType getFromInterfaces(Class clazz)
	{
		if (clazz == null)
		{
			return null;
		}
		for (Class possibleTypeInterface : clazz.getInterfaces())
		{
			EntityType entityType = getEntityType(possibleTypeInterface);
			if (entityType != null)
			{
				return entityType;
			}
		}
		EntityType entityType = getFromInterfaces(clazz.getSuperclass());
		if (entityType != null)
		{
			return entityType;
		}
		return null;
	}

	@Override
	public void initialize(EntityTypeCreationContext entityTypeCreationContext)
	{
		this.setIncludedPackage(Type.class.getPackage().getName());
		this.setEntityClass(Type.class);
		super.initialize(entityTypeCreationContext);
	}

	@Override
	protected void initializeTypeHierachy(AbstractEntityType entityType)
	{
		Class[] interfaces = entityType.getEntityClass().getInterfaces();
		Class superClass = null;
		for (Class possibleSuperType : interfaces)
		{
			if (possibleSuperType != View.class)
			{
				if (superClass != null)
				{
					throw new IllegalStateException("found two interfaces that could be superclass for "
						+ entityType.getCode());
				}
				superClass = possibleSuperType;
			}
		}
		if (superClass != null)
		{
			EntityType superType = getEntityTypeReference(superClass);
			entityType.setSuperEntityType(superType);
			if (superType instanceof AbstractEntityType)
			{
				((AbstractEntityType) superType).addSubEntityType(entityType);
			}
		}
	}

	@Override
	protected boolean isAvailable(Class clazz)
	{
		return Type.class.isAssignableFrom(clazz) || Type.class == clazz;
	}

}
