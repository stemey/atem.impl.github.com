package org.atemsource.atem.impl.common.method;

import javax.annotation.PostConstruct;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;
import org.atemsource.atem.api.method.Method;
import org.atemsource.atem.api.method.ParameterType;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;

public class ParameterTypeImpl extends AbstractEntityType<Object[]> implements ParameterType {

	@Override
	public Object[] createEntity() throws TechnicalException {
		return new Object[getParameterCount()];
	}

	@Override
	public Class<Object[]> getJavaType() {
		return Object[].class;
	}

	@Override
	public boolean isEqual(Object[] a, Object[] b) {
		if (a == null && b == null) {
			return true;
		} else if (a == null) {
			return false;
		} else if (b == null) {
			return false;
		} else {
			return a.equals(b);
		}
	}

	@Override
	public boolean isInstance(Object value) {
		if (value instanceof Object[]) {
			Object[] array = (Object[]) value;
			if (array.length != getAttributes().size()) {
				return false;
			} else {
				for (int index = 0; index < array.length; index++) {
					Attribute<?, ?> attribute = getParameter(index);
					if (!attribute.getReturnType().isInstance(array[index])) {
						return false;
					}
				}
				return true;
			}
		} else {
			return false;
		}
	}

	private Method method;

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@Override
	public int getParameterCount() {
		return getAttributes().size();
	}

	@Override
	public Attribute<?, ?> getParameter(int index) {
		return getAttribute(String.valueOf(index));
	}

	@Override
	public int indexOf(Attribute<?, ?> attribute) {
		return Integer.parseInt(attribute.getCode());
	}

}
