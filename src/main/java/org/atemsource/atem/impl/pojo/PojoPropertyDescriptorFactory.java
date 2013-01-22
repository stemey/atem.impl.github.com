package org.atemsource.atem.impl.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.atemsource.atem.api.attribute.Accessor;
import org.atemsource.atem.impl.pojo.attribute.PojoAccessor;
import org.atemsource.atem.impl.pojo.attribute.PropertyDescriptor;
import org.springframework.beans.BeanUtils;


public class PojoPropertyDescriptorFactory implements PropertyDescriptorFactory
{
	private boolean fieldAccess;

	private PropertyIgnoreHandler propertyIgnoreHandler;

	public PropertyDescriptor createInstance(Class clazz, Field field)
	{
		PojoAccessor pojoAccessor = new PojoAccessor(field, null, null, true);
		return new PropertyDescriptor(field.getName(), pojoAccessor, field.getType(), true, false);
	}

	public PropertyDescriptor createInstance(final Class clazz, String propertyName)
	{
		String name;
		Class type = null;
		Accessor accessor;
		boolean derived = true;
		boolean writable = false;
		try
		{
			Method readMethod;
			Method writeMethod;
			Field field = null;
			Class currentClass = clazz;
			// this is probably necessary because the field is named exactly
			// like the getter
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
				readMethod = propertyDescriptor2.getReadMethod();
				if (readMethod != null)
				{
					type = readMethod.getReturnType();
				}
				writeMethod = propertyDescriptor2.getWriteMethod();
				if (writeMethod != null)
				{
					writable = true;
				}

			}
			else
			{
				return null;
			}

			while (currentClass != null)
			{
				try
				{
					field = clazz.getDeclaredField(propertyName);
					writable = true;
					type = field.getType();
				}
				catch (NoSuchFieldException e)
				{
				}
				currentClass = currentClass.getSuperclass();
			}
			if (type == null)
			{
				return null;
			}
			accessor = new PojoAccessor(field, readMethod, writeMethod, fieldAccess);
			derived = field != null;
		}
		catch (SecurityException e)
		{
			return null;
		}

		return new PropertyDescriptor(propertyName, accessor, type, writable, derived);
	}

	@Override
	public List<PropertyDescriptor> getPropertyDescriptors(Class clazz)
	{
		List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();
		if (fieldAccess)
		{
			for (Field field : clazz.getDeclaredFields())
			{
				// clazz there will be more than one method being returned for
				// generic methods (all used covariant return
				// types)

				String propertyName = field.getName();
				final PropertyDescriptor propertydescriptor = createInstance(clazz, field);
				if (propertydescriptor != null
					&& (propertyIgnoreHandler == null || !propertyIgnoreHandler.isIgnored(propertydescriptor)))
				{
					propertyDescriptors.add(propertydescriptor);
				}
			}
		}
		else
		{
			for (Method method : clazz.getDeclaredMethods())
			{
				// clazz there will be more than one method being returned for
				// generic methods (all used covariant return
				// types)

				if (method.getName().startsWith("get") && method.getName().length() > 3
					&& method.getParameterTypes().length == 0)
				{
					String propertyName = method.getName();
					propertyName = propertyName.substring(3, 4).toLowerCase() + propertyName.substring(4);
					final PropertyDescriptor propertydescriptor = createInstance(clazz, propertyName);
					if (propertydescriptor != null
						&& (propertyIgnoreHandler == null || !propertyIgnoreHandler.isIgnored(propertydescriptor)))
					{
						propertyDescriptors.add(propertydescriptor);
					}
				}
				else if (method.getName().startsWith("is") && method.getName().length() > 2
					&& method.getParameterTypes().length == 0)
				{
					String propertyName = method.getName();
					propertyName = propertyName.substring(2, 3).toLowerCase() + propertyName.substring(3);
					final PropertyDescriptor propertydescriptor = createInstance(clazz, propertyName);
					if (propertydescriptor != null
						&& (propertyIgnoreHandler == null || !propertyIgnoreHandler.isIgnored(propertydescriptor)))
					{
						propertyDescriptors.add(propertydescriptor);
					}
				}
			}
		}
		return propertyDescriptors;
	}

	public boolean isFieldAccess()
	{
		return fieldAccess;
	}

	public void setFieldAccess(boolean fieldAccess)
	{
		this.fieldAccess = fieldAccess;
	}
}
