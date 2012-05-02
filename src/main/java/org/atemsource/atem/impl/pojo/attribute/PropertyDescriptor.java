/*******************************************************************************
 * Stefan Meyer, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo.attribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;


public class PropertyDescriptor
{

	public static PropertyDescriptor createInstance(final Class clazz, String propertyName, boolean fieldAccess)
	{
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor();
		try
		{
			Class currentClass = clazz;
			// this is probably necessary because the field is named exactly like the getter
			if (propertyName.startsWith("is"))
			{
				propertyName = propertyName.substring(2, 3).toLowerCase() + propertyName.substring(3);
			}
			else if (propertyName.startsWith("get"))
			{
				propertyName = propertyName.substring(3, 4).toLowerCase() + propertyName.substring(4);
			}
			java.beans.PropertyDescriptor propertyDescriptor2 = BeanUtils.getPropertyDescriptor(clazz, propertyName);
			if (propertyDescriptor2 != null)
			{
				propertyDescriptor.readMethod = propertyDescriptor2.getReadMethod();
				propertyDescriptor.writeMethod = propertyDescriptor2.getWriteMethod();

			}
			else
			{
				return null;
			}

			while (currentClass != null)
			{
				try
				{
					propertyDescriptor.field = clazz.getDeclaredField(propertyName);
				}
				catch (NoSuchFieldException e)
				{
				}
				currentClass = currentClass.getSuperclass();
			}
		}
		catch (SecurityException e)
		{
			return null;
		}
		propertyDescriptor.propertyName = propertyName;
		if (propertyDescriptor.getReadMethod() != null)
		{
			propertyDescriptor.type = propertyDescriptor.getReadMethod().getReturnType();
		}
		else if (propertyDescriptor.getField() != null)
		{
			propertyDescriptor.type = propertyDescriptor.getField().getType();
		}
		propertyDescriptor.fieldAccess = fieldAccess;
		return propertyDescriptor;
	}

	public static Collection<PropertyDescriptor> getPropertyDescriptors(Class clazz, boolean fieldAccess)
	{
		Set<PropertyDescriptor> propertyDescriptors = new HashSet<PropertyDescriptor>();
		for (Method method : clazz.getDeclaredMethods())
		{
			// clazz there will be more than one method being returned for generic methods (all used covariant return
			// types)

			if (method.getName().startsWith("get") && method.getName().length() > 3
				&& method.getParameterTypes().length == 0)
			{
				String propertyName = method.getName();
				propertyName = propertyName.substring(3, 4).toLowerCase() + propertyName.substring(4);
				final PropertyDescriptor propertydescriptor =
					PropertyDescriptor.createInstance(clazz, propertyName, fieldAccess);
				if (propertydescriptor != null)
				{
					propertyDescriptors.add(propertydescriptor);
				}
			}
			else if (method.getName().startsWith("is") && method.getName().length() > 2
				&& method.getParameterTypes().length == 0)
			{
				String propertyName = method.getName();
				propertyName = propertyName.substring(2, 3).toLowerCase() + propertyName.substring(3);
				final PropertyDescriptor propertydescriptor =
					PropertyDescriptor.createInstance(clazz, propertyName, fieldAccess);
				if (propertydescriptor != null)
				{
					propertyDescriptors.add(propertydescriptor);
				}
			}
		}
		return propertyDescriptors;
	}

	private String propertyName;

	private Method readMethod;

	private Field field;

	private Method writeMethod;

	private Class type;

	private boolean fieldAccess = false;

	public PropertyDescriptor()
	{

	}

	public PropertyDescriptor(String propertyName, Method readMethod, Field field, Method writeMethod, Class type,
		boolean fieldAcess)
	{
		super();
		this.propertyName = propertyName;
		this.readMethod = readMethod;
		this.field = field;
		this.writeMethod = writeMethod;
		this.type = type;
		this.fieldAccess = fieldAcess;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyDescriptor other = (PropertyDescriptor) obj;
		if (propertyName == null)
		{
			if (other.propertyName != null)
				return false;
		}
		else if (!propertyName.equals(other.propertyName))
			return false;
		return true;
	}

	public <A extends Annotation> A getAnnotation(final Class<A> clazz)
	{
		A a = null;

		if (readMethod != null)
		{
			a = readMethod.getAnnotation(clazz);
		}
		if (a == null && field != null)
		{
			a = field.getAnnotation(clazz);
		}
		return a;
	}

	public Field getField()
	{
		return field;
	}

	public String getName()
	{
		return propertyName;
	}

	public Class getPropertyType()
	{
		return type;
	}

	public Method getReadMethod()
	{
		return readMethod;
	}

	public Method getWriteMethod()
	{
		return writeMethod;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		return result;
	}

	public boolean isFieldAccess()
	{
		return fieldAccess;
	}

	public boolean isWritable()
	{
		return writeMethod != null || field != null;
	}

}
