package org.atemsource.atem.impl.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.atemsource.atem.api.attribute.Accessor;
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;

public class AnnotationAccessor implements Accessor {
private Method method;

@Override
public Object getValue(Object entity) {
	try {
		return method.invoke(entity, new Object[0]);
	} catch (IllegalAccessException e) {
		throw new TechnicalException("cannot read from annotation",e);
	} catch (IllegalArgumentException e) {
		throw new TechnicalException("cannot read from annotation",e);
	} catch (InvocationTargetException e) {
		throw new TechnicalException("cannot read from annotation",e);
	}
}

@Override
public boolean isReadable() {
	return true;
}

@Override
public boolean isWritable() {
	return false;
}

@Override
public void setValue(Object entity, Object value) {
	throw new UnsupportedOperationException("cannot write annotations");
}
}
