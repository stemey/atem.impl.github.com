package org.atemsource.atem.impl.common.infrastructure;

import java.lang.annotation.Annotation;
import java.util.Set;


public class CandidateByAnnotationResolver implements CandidateResolver
{

	private Class<? extends Annotation>[] annotationTypes;

	public CandidateByAnnotationResolver()
	{

	}

	public CandidateByAnnotationResolver(Set<Class<? extends Annotation>> annotationTypes)
	{
		this.annotationTypes = annotationTypes.toArray(new Class[0]);
	}

	public Class<? extends Annotation>[] getAnnotationTypes()
	{
		return annotationTypes;
	}

	@Override
	public boolean isCandidate(Class<?> clazz)
	{
		if (annotationTypes.length == 0)
		{
			return true;
		}
		for (Class<? extends Annotation> annotationType : annotationTypes)
		{
			if (clazz.isAnnotationPresent(annotationType))
			{
				return true;
			}
		}
		return false;
	}

	public void setAnnotationTypes(Class<? extends Annotation>[] annotationTypes)
	{
		this.annotationTypes = annotationTypes;
	}

}
