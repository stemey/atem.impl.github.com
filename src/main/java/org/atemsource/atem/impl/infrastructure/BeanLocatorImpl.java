/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.infrastructure;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.atemsource.atem.api.BeanLocator;
import org.atemsource.atem.api.BeanReferenceData;
import org.atemsource.atem.api.BeanReferenceData.BeanReference;
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;
import org.atemsource.atem.api.infrastructure.util.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.stereotype.Service;


@Service("meta_beanLocator")
public class BeanLocatorImpl extends org.atemsource.atem.api.BeanLocator implements BeanFactoryPostProcessor
{

	private ConfigurableListableBeanFactory factory;

	private Map<String, org.atemsource.atem.api.infrastructure.bean.Bean<?>> registeredBeans;

	private ConcurrentHashMap<Class, Set<org.atemsource.atem.api.infrastructure.bean.Bean<?>>> registeredBeansByClass;

	public BeanLocatorImpl()
	{
		registeredBeans = new HashMap<String, org.atemsource.atem.api.infrastructure.bean.Bean<?>>();
		setInstance(this);
	}

	

	@PreDestroy
	void destroy()
	{
		registeredBeans = null;
	}

	public Collection<Bean<?>> filterScopedTargets(Collection<Bean<?>> collection)
	{
		List<Bean<?>> beans = new ArrayList<Bean<?>>();
		for (Bean<?> bean : collection)
		{
			if (!bean.isScopedTarget())
			{
				beans.add(bean);
			}
		}
		return beans;
	}

	/*
	 * (non-Javadoc)
	 * @see org.atemsource.atem.impl.infrastructure.BL#getBean(org.atemsource.atem.impl.common.meta.BeanReferenceData)
	 */

	@Override
	public org.atemsource.atem.api.infrastructure.bean.Bean<?> getBean(BeanReferenceData beanReferenceData)
	{
		if (beanReferenceData == null)
		{
			return null;
		}
		if (StringUtils.isNotEmpty(beanReferenceData.getBeanName()))
		{
			return getBean(beanReferenceData.getBeanName());
		}
		else if (beanReferenceData.getBeanClass() != null && !beanReferenceData.getBeanClass().equals(Object.class))
		{
			return getBean(beanReferenceData.getBeanClass());
		}
		else if (beanReferenceData.getQualifier() != null && !(beanReferenceData.getQualifier().equals(Annotation.class)))
		{
			final Class<Annotation> qualifierAnnotation = (Class<Annotation>) beanReferenceData.getQualifier();
			Qualifier qualifier = ReflectionUtils.getAnnotatedAnnotation(qualifierAnnotation, Qualifier.class);
			if (qualifier != null)
			{
				return getInstance(qualifier.value());
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.atemsource.atem.impl.infrastructure.BL#getBean(java.lang.Class)
	 */
	@Override
	public <T> org.atemsource.atem.api.infrastructure.bean.Bean<T> getBean(Class<T> beanType)
	{
		Set<org.atemsource.atem.api.infrastructure.bean.Bean> beans = getBeansFromCache(beanType);
		switch (beans.size())
		{
			case 0:
				throw new IllegalStateException("there is not any bean for type " + beanType.getName());
			case 1:
				return (Bean<T>) beans.iterator().next();
			case 2:
				Iterator<org.atemsource.atem.api.infrastructure.bean.Bean> beanIterator = beans.iterator();
				org.atemsource.atem.api.infrastructure.bean.Bean bean1 = beanIterator.next();
				org.atemsource.atem.api.infrastructure.bean.Bean bean2 = beanIterator.next();
				if (bean1.isScopedTarget())
				{
					return (org.atemsource.atem.api.infrastructure.bean.Bean<T>) bean1;
				}
				else if (bean2.isScopedTarget())
				{
					return (org.atemsource.atem.api.infrastructure.bean.Bean<T>) bean2;
				}
				else
				{
					throw new IllegalStateException("there is not a unique bean for type " + beanType.getName() + ". but "
						+ beans.size() + ".");

				}
			default:
				throw new IllegalStateException("there is not a unique bean for type " + beanType.getName() + ". but "
					+ beans.size() + ".");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.atemsource.atem.impl.infrastructure.BL#getBean(java.lang.String)
	 */
	@Override
	public org.atemsource.atem.api.infrastructure.bean.Bean<?> getBean(String beanName)
	{
		return registeredBeans.get(beanName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.atemsource.atem.impl.infrastructure.BL#getBeanName(java.lang.Object)
	 */
	@Override
	public String getBeanName(Object object)
	{
		for (org.atemsource.atem.api.infrastructure.bean.Bean<?> bean : getBeans(object.getClass()))
		{
			if (bean.existsInScope() && bean.get() == object)
			{
				return bean.getBeanName();
			}
		}
		return null;
	}

		public  Set<org.atemsource.atem.api.infrastructure.bean.Bean<?>> getBeansInternally(Class<?> beanType)
	{
		Set beansFromCache = getBeansFromCache(beanType);
		return beansFromCache;
	}

	public <T>  Set<org.atemsource.atem.api.infrastructure.bean.Bean<T>> getBeans(Class<T> beanType)
	{
		Set beansFromCache = getBeansFromCache(beanType);
		return beansFromCache;
	}

	@SuppressWarnings("unchecked")
	private Set<org.atemsource.atem.api.infrastructure.bean.Bean> getBeansFromCache(Class<?> beanType)
	{
		// we need to create the cache on-demand because factory.getBeansOfType does not work at starrtup time for beans
		// that are in Web-Scopes.
		Set<org.atemsource.atem.api.infrastructure.bean.Bean> beans = new HashSet<org.atemsource.atem.api.infrastructure.bean.Bean>();
		 Set<org.atemsource.atem.api.infrastructure.bean.Bean<?>> set = registeredBeansByClass.get(beanType);
		if (set == null)
		{
			Set<org.atemsource.atem.api.infrastructure.bean.Bean<?>> beansFromSpring = getBeansFromSpring(beanType);
			Set<org.atemsource.atem.api.infrastructure.bean.Bean<?>> untypedBeans = new HashSet<org.atemsource.atem.api.infrastructure.bean.Bean<?>>();
			untypedBeans.addAll(beansFromSpring);
			registeredBeansByClass.put(beanType, untypedBeans);
			set=untypedBeans;
		}
			for (org.atemsource.atem.api.infrastructure.bean.Bean<?> bean : set)
			{
				beans.add((org.atemsource.atem.api.infrastructure.bean.Bean<?>) bean);
			}
			return beans;
		
	}

	private  Set<org.atemsource.atem.api.infrastructure.bean.Bean<?>> getBeansFromSpring(Class<?> beanType)
	{
		String[] beanNames = factory.getBeanNamesForType(beanType);
		Set<org.atemsource.atem.api.infrastructure.bean.Bean<?>> result = new HashSet<org.atemsource.atem.api.infrastructure.bean.Bean<?>>();
		for (String beanName : beanNames)
		{
			result.add((Bean<?>) getBean(beanName));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.atemsource.atem.impl.infrastructure.BL#getInstance(org.atemsource.atem.impl.common.meta.BeanReferenceData)
	 */
	@Override
	public Object getInstance(BeanReferenceData beanReferenceData)
	{
		return getBean(beanReferenceData).get();
	}

	/*
	 * (non-Javadoc)
	 * @see org.atemsource.atem.impl.infrastructure.BL#getInstance(java.lang.Class)
	 */
	@Override
	public <T> T getInstance(Class<T> beanType)
	{
		return getBean(beanType).get();
	}

	/*
	 * (non-Javadoc)
	 * @see org.atemsource.atem.impl.infrastructure.BL#getInstance(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getInstance(String configurationId)
	{
		Bean<T> bean = (Bean<T>) getBean(configurationId);
		if (bean != null)
		{
			return bean.get();
		}
		else
		{
			throw new IllegalArgumentException("Can not locate bean for name '" + configurationId + "'");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.atemsource.atem.impl.infrastructure.BL#getInstances(java.lang.Class)
	 */
	@Override
	public <T> Collection<T> getInstances(Class<T> beanType)
	{
		Set beans = getBeans(beanType);
		List<T> instances = new ArrayList<T>();
		for (Object o : beans)
		{
			Bean<T> bean=(Bean<T>) o;
			if (!bean.isScopedTarget())
			{
				instances.add((T) bean.get());
			}
		}
		return instances;
	}


	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
	{
		this.factory = factory;

		String[] allBeanNames = factory.getBeanDefinitionNames();
		registeredBeansByClass = new ConcurrentHashMap<Class, Set<org.atemsource.atem.api.infrastructure.bean.Bean<?>>>();
		for (String beanName : allBeanNames)
		{
			Bean<?> bean = new Bean<Object>(beanName, factory.getBeanDefinition(beanName));
			// if (beanName.startsWith("scopedTarget."))
			// {
			// beanName = beanName.substring("scopedTarget.".length());
			// }
			registeredBeans.put(beanName, bean);
		}
		registeredBeans = Collections.unmodifiableMap(registeredBeans);
	}

	/*
	 * (non-Javadoc)
	 * @see org.atemsource.atem.impl.infrastructure.BL#remove(org.atemsource.atem.impl.infrastructure.BeanLocator.Bean)
	 */
	@Override
	public void remove(org.atemsource.atem.api.infrastructure.bean.Bean<?> bean)
	{

		Bean<?> thisBean = (Bean<?>) bean;
		if (thisBean.containingScope != null)
		{
			thisBean.containingScope.remove(bean.getBeanName());
		}
	}

	/**
	 * Resolvable reference to a bean instance with additional functionality.
	 * 
	 * @author sven
	 */
	public class Bean<T> implements org.atemsource.atem.api.infrastructure.bean.Bean<T>
	{

		private final String beanName;

		private final BeanContainingScopeDecorator containingScope;

		private Class<T> beanClass;

		public Bean(String beanName, BeanDefinition beanDefinition)
		{
			this.beanName = beanName;
			if (beanDefinition.getBeanClassName() != null)
			{
				Class<?> rawType;
				try
				{

					rawType = Class.forName(beanDefinition.getBeanClassName());
				}
				catch (ClassNotFoundException e)
				{
					throw new TechnicalException("cannot find class of bean " + beanName, e);
				}
				if (!FactoryBean.class.isAssignableFrom(rawType))
				{
					beanClass = (Class<T>) rawType;
				}
				else
				{
					FactoryBean factoryBean = (FactoryBean) factory.getBean("&" + beanName);
					beanClass = (Class<T>) factoryBean.getObjectType();
				}
			}

			this.containingScope = checkScope(beanName);
		}

		private BeanContainingScopeDecorator checkScope(String beanName)
		{
			BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
			String scopeName = beanDefinition.getScope();
			if (scopeName != null)
			{
				Scope scope = factory.getRegisteredScope(scopeName);
				if (scope instanceof BeanContainingScopeDecorator)
				{
					return (BeanContainingScopeDecorator) scope;
				}
			}
			return null;
		}

		/**
		 * @return true, if the instance exists in the configured scope, false otherwise
		 */
		@Override
		public boolean existsInScope()
		{
			if (containingScope != null)
			{
				return containingScope.containsBean(beanName);
			}
			else
			{
				return true;
			}
		}

		/**
		 * @return the instance of this bean, creating it, if it does not exists
		 */
		@Override
		@SuppressWarnings("unchecked")
		public T get()
		{
			return (T) factory.getBean(beanName);
		}

		@Override
		public Class<T> getBeanClass()
		{
			return beanClass;
		}

		@Override
		public String getBeanName()
		{
			return beanName;
		}

		@Override
		public boolean isScopedTarget()
		{
			return beanName.startsWith("scopedTarget");
		}

		@Override
		public void remove()
		{
			if (containingScope != null)
			{
				containingScope.remove(beanName);
			}
		}

	}
}
