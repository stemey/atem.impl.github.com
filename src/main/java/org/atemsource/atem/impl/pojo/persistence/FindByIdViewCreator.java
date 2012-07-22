package org.atemsource.atem.impl.pojo.persistence;

import java.io.Serializable;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.type.EntityType;
import org.springframework.stereotype.Component;

@Component
public class FindByIdViewCreator implements ViewCreator {

	@Inject
	private EntityTypeRepository entityTypeRepository;

	@Override
	public Serializable[] getKeys(Object entity) {
		EntityType<Object> entityType = entityTypeRepository
				.getEntityType(entity);
		IdentityService service = entityType.getService(IdentityService.class);
		return new Serializable[] { entityType.getCode(),
				service.getId(entityType, entity) };
	}

	@Override
	public Serializable getKeyForParameter(Object[] parameter) {
		return parameter;
	}

	@Override
	public Class[] getParameterTypes() {
		return new Class<?>[] { String.class, Serializable.class };
	}

}
