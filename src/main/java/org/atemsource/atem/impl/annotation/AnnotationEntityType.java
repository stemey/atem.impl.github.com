package org.atemsource.atem.impl.annotation;

import java.lang.annotation.Annotation;

import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;

public class AnnotationEntityType<A extends Annotation> extends AbstractEntityType<A>{

	@Override
	public Class<A> getJavaType()
	{
		return getEntityClass();
	}

	@Override
	public boolean isInstance(Object value) {
		return value instanceof Annotation;
	}




}
