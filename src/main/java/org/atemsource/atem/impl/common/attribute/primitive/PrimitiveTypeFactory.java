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
package org.atemsource.atem.impl.common.attribute.primitive;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.atemsource.atem.api.BeanLocator;
import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.api.type.PrimitiveTypeRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PrimitiveTypeFactory
{

	private Collection<Class> classes;

	Map<Class<?>, PrimitiveType> classToType = new HashMap<Class<?>, PrimitiveType>();

	@Autowired
	private BeanLocator beanLocator;

	public Collection<Class> getClasses()
	{
		return classes;
	}

	public PrimitiveType getPrimitiveType(Class primitiveType)
	{
		if (String.class.isAssignableFrom(primitiveType))
		{
			return new SimpleTextType();
		}
		else if (Boolean.TYPE.isAssignableFrom(primitiveType))
		{
			final BooleanTypeImpl booleanTypeImpl = new BooleanTypeImpl();
			booleanTypeImpl.setNullable(false);
			return booleanTypeImpl;
		}
		else if (Boolean.class.isAssignableFrom(primitiveType))
		{
			return new BooleanTypeImpl();
		}
		else if (double.class.isAssignableFrom(primitiveType))
		{
			final DoubleType doubleTypeImpl = new DoubleType();
			doubleTypeImpl.setNullable(false);
			return doubleTypeImpl;
		}
		else if (Double.class.isAssignableFrom(primitiveType))
		{
			return new DoubleType();
		}
		else if (float.class.isAssignableFrom(primitiveType))
		{
			final FloatTypeImpl typeImpl = new FloatTypeImpl();
			typeImpl.setNullable(false);
			return typeImpl;
		}
		else if (Float.class.isAssignableFrom(primitiveType))
		{
			return new FloatTypeImpl();
		}
		else if (BigDecimal.class.isAssignableFrom(primitiveType))
		{
			return new BigDecimalTypeImpl();
		}
		else if (int.class.isAssignableFrom(primitiveType))
		{
			final IntegerType integerTypeImpl = new IntegerType();
			integerTypeImpl.setNullable(false);
			return integerTypeImpl;
		}
		else if (Integer.class.isAssignableFrom(primitiveType))
		{
			return new IntegerType();
		}
		else if (long.class.isAssignableFrom(primitiveType))
		{
			return new LongType(false);
		}
		else if (Long.class.isAssignableFrom(primitiveType))
		{
			return new LongType(true);
		}
		else if (Number.class.isAssignableFrom(primitiveType))
		{
			return new SimplePrimitiveType(Number.class, true);
		}
		else if (Character.TYPE.isAssignableFrom(primitiveType))
		{
			return new SimplePrimitiveType(Character.TYPE, false);
		}
		else if (Character.class.isAssignableFrom(primitiveType))
		{
			return new SimplePrimitiveType(Character.class, true);
		}
		else if (BigDecimal.class.isAssignableFrom(primitiveType))
		{
			return new SimplePrimitiveType(BigDecimal.class, true);
		}
		else if (BigInteger.class.isAssignableFrom(primitiveType))
		{
			return new SimplePrimitiveType(BigInteger.class, true);
		}
		else if (Byte.TYPE.isAssignableFrom(primitiveType))
		{
			return new SimplePrimitiveType(Byte.TYPE, false);
		}
		else if (byte[].class.isAssignableFrom(primitiveType))
		{
			return new SimplePrimitiveType(byte[].class, true);
		}
		else if (Byte.class.isAssignableFrom(primitiveType))
		{
			return new SimplePrimitiveType(Byte.class, true);
		}
		else if (Enum.class.isAssignableFrom(primitiveType))
		{
			return new SimpleEnumType(primitiveType);
		}
		else
		{
			PrimitiveType found = classToType.get(primitiveType);
			Class parentClass = primitiveType;
			while (found == null && parentClass.getSuperclass() != null)
			{
				found = classToType.get(parentClass);
				parentClass = parentClass.getSuperclass();
			}
			if (found == null)
			{
				for (Class<?> interfaze : primitiveType.getInterfaces())
				{
					found = classToType.get(interfaze);
					if (found != null)
					{
						return found;
					}
				}
			}
			return found;
		}
	}

	@PostConstruct
	public void initialize()
	{
		classes = new ArrayList<Class>();
		classes.add(String.class);
		classes.add(Boolean.class);
		classes.add(Boolean.TYPE);
		classes.add(Long.class);
		classes.add(Long.TYPE);
		classes.add(Integer.class);
		classes.add(Integer.TYPE);
		classes.add(Float.class);
		classes.add(Float.TYPE);
		classes.add(Double.TYPE);
		classes.add(Double.class);
		classes.add(Character.TYPE);
		classes.add(Character.class);
		classes.add(Character.TYPE);
		classes.add(Character.class);
		classes.add(Byte.TYPE);
		classes.add(Byte.class);
		classes.add(Enum.class);
		classes.add(BigInteger.class);
		classes.add(BigDecimal.class);
		Collection<PrimitiveTypeRegistrar> registrars = beanLocator.getInstances(PrimitiveTypeRegistrar.class);
		for (PrimitiveTypeRegistrar registrar : registrars)
		{
			PrimitiveType<?>[] types = registrar.getTypes();
			for(PrimitiveType<?> primitiveType:types) {
			classToType.put(primitiveType.getJavaType(), primitiveType);
			classes.add(primitiveType.getJavaType());
			}
		}
	}

	public void registerType(Class<?> clazz, PrimitiveType primitiveType)
	{
		classToType.put(clazz, primitiveType);
		classes.add(clazz);
	}
}
