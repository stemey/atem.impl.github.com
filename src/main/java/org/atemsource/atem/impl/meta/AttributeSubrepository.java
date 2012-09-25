package org.atemsource.atem.impl.meta;

import org.atemsource.atem.api.attribute.AssociationAttribute;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.view.View;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.infrastructure.CandidateResolver;
import org.atemsource.atem.impl.pojo.ScannedPojoEntityTypeRepository;
import org.atemsource.atem.spi.EntityTypeCreationContext;

public class AttributeSubrepository extends ScannedPojoEntityTypeRepository {

	@Override
	protected void initializeTypeHierachy(AbstractEntityType entityType) {
		Class[] interfaces = entityType.getEntityClass().getInterfaces();
		Class superClass = null;
		for (Class possibleSuperType : interfaces) {
			if (possibleSuperType != AssociationAttribute.class) {
				if (superClass != null) {
					throw new IllegalStateException(
							"found two interfaces that could be superclass for "
									+ entityType.getCode());
				}
				superClass = possibleSuperType;
			}
		}
		if (superClass != null) {
			EntityType superType = getEntityTypeReference(superClass);
			entityType.setSuperEntityType(superType);
			if (superType instanceof AbstractEntityType) {
				((AbstractEntityType) superType).addSubEntityType(entityType);
			}
		}
	}

	@Override
	public EntityType getEntityType(Class clazz) {
		EntityType entityType = super.getEntityType(clazz);
		if (entityType == null) {
			entityType = getFromInterfaces(clazz);
			classToEntityTypes.put(clazz, entityType);
			nameToEntityTypes.put(clazz.getName(),
					(AbstractEntityType<Object>) entityType);
		}
		return entityType;
	}

	protected EntityType getFromInterfaces(Class clazz) {
		if (clazz==null) {
			return null;
		}
		for (Class possibleTypeInterface:clazz.getInterfaces()) {
			EntityType entityType=getEntityType(possibleTypeInterface);
			if (entityType!=null) {
				return entityType;
			}
		}
		EntityType entityType=getFromInterfaces(clazz.getSuperclass());
		if (entityType!=null) {
			return entityType;
		}
		return null;
	}

	@Override
	public EntityType<Object> getEntityType(Object entity) {
		return getEntityType(entity.getClass());
	}

	@Override
	public void initialize(EntityTypeCreationContext entityTypeCreationContext) {
		this.setIncludedPackage(Attribute.class.getPackage().getName());
		this.setEntityClass(Attribute.class);
		setCandidateResolver(new CandidateResolver() {

			@Override
			public boolean isCandidate(Class<?> clazz) {
				return isAvailable(clazz);
			}
		});
		super.initialize(entityTypeCreationContext);
	}

	@Override
	protected boolean isAvailable(Class clazz) {
		return Attribute.class.isAssignableFrom(clazz);
	}

}
