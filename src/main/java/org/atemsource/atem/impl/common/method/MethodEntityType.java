package org.atemsource.atem.impl.common.method;

import org.atemsource.atem.api.method.Method;
import org.atemsource.atem.impl.common.AbstractEntityType;

public class MethodEntityType extends AbstractEntityType<Method>{

	@Override
	public Class<Method> getJavaType() {
		return Method.class;
	}

	@Override
	public boolean isEqual(Method a, Method b) {
		return a.equals(b);
	}

	@Override
	public boolean isInstance(Object value) {
		return value instanceof Method;
	}

}
