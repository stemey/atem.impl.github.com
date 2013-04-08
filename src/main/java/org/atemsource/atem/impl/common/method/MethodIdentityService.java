package org.atemsource.atem.impl.common.method;

import java.io.Serializable;

import org.atemsource.atem.api.method.Method;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.type.EntityType;

public class MethodIdentityService implements IdentityService {

	@Override
	public Serializable getId(EntityType<?> entityType, Object entity) {
		if (entity == null) {
			return null;
		} else {
			return ((Method) entity).getCode();
		}
	}

}
