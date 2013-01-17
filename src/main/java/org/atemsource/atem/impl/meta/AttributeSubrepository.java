package org.atemsource.atem.impl.meta;

import org.atemsource.atem.api.attribute.AssociationAttribute;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.MetaLogs;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.infrastructure.CandidateResolver;
import org.atemsource.atem.impl.pojo.ScannedPojoEntityTypeRepository;
import org.atemsource.atem.spi.EntityTypeCreationContext;


public class AttributeSubrepository extends ScannedPojoEntityTypeRepository
{

	private boolean scanning;

	@Override
	public EntityType getEntityType(Class clazz)
	{
		if (!isAvailable(clazz))
		{
			return null;
		}
		EntityType entityType = super.getEntityType(clazz);
		if (entityType == null)
		{
			entityType = getFromInterfaces(clazz);
			if (entityType != null && !classToEntityTypes.containsKey(classToEntityTypes))
			{
				if (!scanning)
				{
					classToEntityTypes.put(clazz, entityType);
					nameToEntityTypes.put(clazz.getName(), (AbstractEntityType<Object>) entityType);
				}
				else
				{
					// TODO fix this
					MetaLogs.LOG
						.warn("cannot create attribute type for "
							+ clazz
							+ " because scanning is running. This is probably because of attribute types being resolve while attributes for entityTypes are build.");
				}
			}
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
		this.setIncludedPackage(Attribute.class.getPackage().getName());
		this.setEntityClass(Attribute.class);
		setCandidateResolver(new CandidateResolver() {

			@Override
			public boolean isCandidate(Class<?> clazz)
			{
				return isAvailable(clazz);
			}
		});
		scanning = true;

		super.initialize(entityTypeCreationContext);
		scanning = false;
	}

	@Override
	protected void initializeTypeHierachy(AbstractEntityType entityType)
	{
		Class[] interfaces = entityType.getEntityClass().getInterfaces();
		Class superClass = null;
		for (Class possibleSuperType : interfaces)
		{
			if (possibleSuperType != AssociationAttribute.class)
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
		return Attribute.class.isAssignableFrom(clazz);
	}

}
