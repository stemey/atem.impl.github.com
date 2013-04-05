package org.atemsource.atem.impl.common.method;

import java.lang.reflect.InvocationTargetException;

import org.atemsource.atem.api.method.Method;
import org.atemsource.atem.api.method.ParameterType;
import org.atemsource.atem.api.type.Type;

public class MethodImpl implements Method {
	private ParameterType parameterType;

	private java.lang.reflect.Method method;

	public java.lang.reflect.Method getMethod() {
		return method;
	}

	public void setMethod(java.lang.reflect.Method method) {
		this.method = method;
	}

	private Type<?> returnType;

	public ParameterType getParameterType() {
		return parameterType;
	}

	public void setParameterType(ParameterType parameterType) {
		this.parameterType = parameterType;
	}

	public Type<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Type<?> returnType) {
		this.returnType = returnType;
	}

	@Override
	public Object invoke(Object target, Object[] param) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return method.invoke(target, param);
	}
}
