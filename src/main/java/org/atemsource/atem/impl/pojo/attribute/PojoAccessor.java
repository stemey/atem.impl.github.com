/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo.attribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.atemsource.atem.api.attribute.Accessor;
import org.atemsource.atem.api.attribute.JavaMetaData;
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;


public class PojoAccessor implements Accessor, JavaMetaData
{

	private boolean allowFieldAccess;

	private Field field;

	private Method readMethod;

	private Method writeMethod;

	public PojoAccessor(Field field, Method readMethod, Method writeMethod, boolean allowFieldAccess)
	{
		super();
		this.readMethod = readMethod;
		this.writeMethod = writeMethod;
		this.field = field;
		this.allowFieldAccess = allowFieldAccess;
	}

	public PojoAccessor(Method readMethod)
	{
		this.readMethod = readMethod;
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass)
	{
		A a = null;
		if (field != null)
		{
			a = field.getAnnotation(annotationClass);
		}
		if (a == null && readMethod != null)
		{
			a = readMethod.getAnnotation(annotationClass);
		}
		return a;
	}

	@Override
	public Annotation getAnnotationAnnotatedBy(Class<? extends Annotation> annotationClass)
	{
		if (field != null)
		{
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations)
			{
				if (annotation.annotationType().isAnnotationPresent(annotationClass))
				{
					return annotation;
				}
			}
		}
		if (readMethod != null)
		{
			Annotation[] annotations = readMethod.getAnnotations();
			for (Annotation annotation : annotations)
			{
				if (annotation.annotationType().isAnnotationPresent(annotationClass))
				{
					return annotation;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<Annotation> getAnnotations()
	{
		Set<Annotation> annotations = new HashSet<Annotation>();
		if (field != null)
		{
			annotations.addAll(Arrays.asList(field.getAnnotations()));
		}
		if (readMethod != null)
		{
			annotations.addAll(Arrays.asList(readMethod.getAnnotations()));
		}
		return annotations;
	}

	public Field getField()
	{
		return field;
	}

	public Method getReadMethod()
	{
		return readMethod;
	}

	@Override
	public Object getValue(Object entity)
	{
		try
		{
			Object[] args;
			args = new Object[0];
			if (entity == null)
			{
				throw new NullPointerException("cannot get attribute value from null");
			}
			else if (readMethod == null && field != null)
			{
				field.setAccessible(true);
				return field.get(entity);
			}
			else
			{

				return readMethod.invoke(entity, args);
			}
		}
		catch (IllegalAccessException e)
		{
			throw new IllegalStateException("please check correct implementation of property at startup time", e);
		}
		catch (InvocationTargetException e)
		{
			throw new IllegalStateException("please check correct implementation of property at startup time", e);
		}
		catch (IllegalArgumentException e)
		{
			throw new IllegalStateException("please check correct implementation of property at startup time", e);
		}
	}

	public Method getWriteMethod()
	{
		return writeMethod;
	}

	@Override
	public boolean isReadable()
	{
		return readMethod != null || (field != null && allowFieldAccess);
	}

	@Override
	public boolean isWritable()
	{
		return writeMethod != null || (field != null && allowFieldAccess);
	}

	public void setField(Field field)
	{
		this.field = field;
	}

	public void setReadMethod(Method readMethod)
	{
		this.readMethod = readMethod;
	}

	@Override
	public void setValue(Object entity, Object value)
	{
		if (writeMethod == null)
		{
			field.setAccessible(true);
			try
			{
				field.set(entity, value);
			}
			catch (IllegalArgumentException e)
			{
				throw new TechnicalException("cannto set attribute", e);
			}
			catch (IllegalAccessException e)
			{
				throw new TechnicalException("cannto set attribute", e);
			}
		}
		else
		{
			Object[] args;
			args = new Object[]{value};
			try
			{
				writeMethod.invoke(entity, args);
			}
			catch (IllegalArgumentException e)
			{
				throw new TechnicalException("cannto set attribute", e);
			}
			catch (IllegalAccessException e)
			{
				throw new IllegalStateException("please check correct implementation of property at startup time", e);
			}
			catch (InvocationTargetException e)
			{
				throw new TechnicalException("cannto set attribute", e);
			}
		}
	}

	public void setWriteMethod(Method writeMethod)
	{
		this.writeMethod = writeMethod;
	}

}
