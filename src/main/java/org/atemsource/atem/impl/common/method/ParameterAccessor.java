package org.atemsource.atem.impl.common.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.atemsource.atem.api.attribute.Accessor;
import org.atemsource.atem.api.attribute.JavaMetaData;

public class ParameterAccessor implements Accessor, JavaMetaData {

	
	public ParameterAccessor(Method method, int index) {
		super();
		this.method = method;
		this.index = index;
	}

	private Method method;
	private int index;
	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		 Annotation[] annotations = method.getParameterAnnotations()[index];
		 for (Annotation annotation:annotations) {
			 if (annotationClass.isInstance(annotation)) {
				 return (A) annotation;
			 }
		 }
		 return null;
	}

	@Override
	public Object getValue(Object entity) {
		Object[] params = (Object[]) entity;
		return params[index];
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWritable() {
		return true;
	}

	@Override
	public void setValue(Object entity, Object value) {
		Object[] params = (Object[]) entity;
		params[index]=value;
	}

	@Override
	public Annotation getAnnotationAnnotatedBy(Class<? extends Annotation> annotationClass) {
		return null;
	}

	@Override
	public Collection<? extends Annotation> getAnnotations() {
		return Arrays.asList(method.getParameterAnnotations()[index]);
	}

}
