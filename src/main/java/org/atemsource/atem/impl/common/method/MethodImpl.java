package org.atemsource.atem.impl.common.method;

import java.lang.reflect.InvocationTargetException;

import org.atemsource.atem.api.method.Method;
import org.atemsource.atem.api.method.ParameterType;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;

public class MethodImpl implements Method {
	private ParameterType parameterType;

	private EntityType<?> entityType;

	public EntityType<?> getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType<?> entityType) {
		this.entityType = entityType;
	}

	private java.lang.reflect.Method javaMethod;

	public java.lang.reflect.Method getJavaMethod() {
		return javaMethod;
	}

	public void setJavaMethod(java.lang.reflect.Method method) {
		this.javaMethod = method;
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

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setReturnType(Type<?> returnType) {
		this.returnType = returnType;
	}

	@Override
	public Object invoke(Object target, Object[] param) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		return javaMethod.invoke(target, param);
	}

}
