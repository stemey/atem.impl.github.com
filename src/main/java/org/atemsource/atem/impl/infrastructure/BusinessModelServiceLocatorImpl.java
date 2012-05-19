/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.infrastructure;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.atemsource.atem.api.BeanLocator;
import org.atemsource.atem.api.infrastructure.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Service("meta_BusinessModelServiceLocator")
public class BusinessModelServiceLocatorImpl implements BusinessModelServiceLocator
{
	@Autowired
	private BeanLocator beanLocator;

	private Map<Class<? extends QualifiedService>, Map<Object, QualifiedService>> qualifiedServices =
		new HashMap<Class<? extends QualifiedService>, Map<Object, QualifiedService>>();

	private final Map<Class<? extends BusinessModelService>, Map<Object, BusinessModelService>> simpleServices =
		new HashMap<Class<? extends BusinessModelService>, Map<Object, BusinessModelService>>();

	@Override
	public <I, S extends QualifiedService<I>> S getQualifiedService(I identifier, Class<S> clazz)
	{
		final Map<Object, QualifiedService> identifiersToInstance = qualifiedServices.get(clazz);
		if (identifiersToInstance == null)
		{
			return null;
		}
		else
		{
			final QualifiedService qualifiedService = identifiersToInstance.get(identifier);
			if (qualifiedService == null)
			{
				return (S) identifiersToInstance.get(null);
			}
			else
			{
				return (S) qualifiedService;
			}
		}
	}

	@Override
	public <S extends BusinessModelService<?>> S getService(Class<?> modelClass, Class<S> s)
	{
		final Map<Object, BusinessModelService> map = simpleServices.get(s);
		if (map == null)
		{
			return null;
		}
		else
		{
			S service = null;
			do
			{
				service = (S) map.get(modelClass);
				if (service != null)
				{
					return service;
				}
				modelClass = modelClass.getSuperclass();
			}
			while (modelClass != null && modelClass != Object.class);
			return (S) map.get(Object.class);
		}
	}

	@Override
	public <S extends BusinessModelService<?>> S getService(Object model, Class<S> s)
	{
		final Map<Object, BusinessModelService> map = simpleServices.get(s);
		if (map == null)
		{
			return null;
		}
		else
		{
			return (S) map.get(model.getClass());
		}
	}

	@PostConstruct
	public void initialize()
	{

		final Collection<BusinessModelService> businessModelServices =
			beanLocator.getInstances(BusinessModelService.class);
		for (final BusinessModelService businessModelService : businessModelServices)
		{
			final Class clazz = businessModelService.getClass();
			final Class identifierType = ReflectionUtils.getActualTypeParameter(clazz, BusinessModelService.class);
			for (final Class interfaze : ReflectionUtils.getAllInterfaces(businessModelService.getClass()))
			{
				if (interfaze != BusinessModelService.class)
				{
					Map<Object, BusinessModelService> interfazeToServices = simpleServices.get(interfaze);
					if (interfazeToServices == null)
					{
						interfazeToServices = new HashMap<Object, BusinessModelService>();
						simpleServices.put(interfaze, interfazeToServices);
					}
					interfazeToServices.put(identifierType, businessModelService);
				}
			}
		}
		final Collection<QualifiedService> unsortedQualifiedInstances = beanLocator.getInstances(QualifiedService.class);
		final Set<QualifiedService> qualifiedInstances = new HashSet<QualifiedService>(unsortedQualifiedInstances);
		for (final QualifiedService qualifiedService : qualifiedInstances)
		{
			final Object[] identifiers = qualifiedService.getQualifiers();
			for (final Class interfaze : ReflectionUtils.getAllInterfacesAndClasses(qualifiedService.getClass()))
			{
				if (interfaze != QualifiedService.class)
				{
					Map<Object, QualifiedService> identifierToServices = qualifiedServices.get(interfaze);
					if (identifierToServices == null)
					{
						identifierToServices = new HashMap<Object, QualifiedService>();
						qualifiedServices.put(interfaze, identifierToServices);
					}
					if (identifiers == null)
					{
						identifierToServices.put(null, qualifiedService);
					}
					else
					{
						for (final Object identifier : identifiers)
						{
							identifierToServices.put(identifier, qualifiedService);
						}
					}
				}
			}
		}
	}
}
