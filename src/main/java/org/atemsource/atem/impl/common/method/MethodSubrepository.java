package org.atemsource.atem.impl.common.method;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.method.Method;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.AbstractMetaDataRepository;
import org.atemsource.atem.spi.EntityTypeCreationContext;

public class MethodSubrepository extends AbstractMetaDataRepository<Method> {

	private MethodEntityType methodEntityType;

	@Override
	public void afterFirstInitialization(EntityTypeRepository entityTypeRepositoryImpl) {
	}

	private void addEntityTypeToLookup(final Class clazz, AbstractEntityType entityType) {
		nameToEntityTypes.put(clazz.getName(), entityType);
		classToEntityTypes.put(clazz, entityType);
		entityTypes.add(entityType);
	}

	@Override
	public void initialize(EntityTypeCreationContext entityTypeCreationContext) {
		methodEntityType = new MethodEntityType();
		methodEntityType.setCode(Method.class.getName());
		addEntityTypeToLookup(Method.class, methodEntityType);
	}

	@Override
	public void afterInitialization() {
	}

	@Override
	public EntityType<Method> getEntityType(Object entity) {
		if (entity instanceof Method) {
			return methodEntityType;
		} else {
			return null;
		}
	}

}
