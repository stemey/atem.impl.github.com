/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.infrastructure;

import java.util.Collection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;


@Service
@Deprecated
public class BeanCreator implements ApplicationContextAware
{

	private ApplicationContext applicationContext;

	private static BeanCreator INSTANCE;

	public BeanCreator()
	{
		super();
		INSTANCE = this;
	}

	public static BeanCreator getInstance()
	{
		// TODO Auto-generated method stub
		return INSTANCE;
	}

	public <T> T create(final Class<T> clazz)
	{
		String[] beansOfType = applicationContext.getBeanNamesForType(clazz);
		if (beansOfType.length == 1)
		{
			return (T) applicationContext.getBean(beansOfType[0]);
		}
		else
		{
			for (String name : beansOfType)
			{
				if (name.equalsIgnoreCase(clazz.getSimpleName()))
				{
					return (T) applicationContext.getBean(name);
				}
			}
			throw new IllegalArgumentException("there are " + beansOfType.length + " bean definitions for class "
				+ clazz.getSimpleName());
		}

	}

	public Object create(String beanName)
	{
		return applicationContext.getBean(beanName);
	}

	public String getBeanName(Class<?> class1)
	{
		String[] beanNamesForType = applicationContext.getBeanNamesForType(class1);
		return beanNamesForType[0];
	}

	public <T> Collection<T> getBeans(Class<T> class1)
	{
		return applicationContext.getBeansOfType(class1).values();
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

}
