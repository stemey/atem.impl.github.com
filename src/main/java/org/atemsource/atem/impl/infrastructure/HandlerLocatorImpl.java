/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.infrastructure;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.atemsource.atem.api.BeanLocator;
import org.atemsource.atem.api.infrastructure.bean.Bean;
import org.atemsource.atem.api.infrastructure.util.ReflectionUtils;
import org.atemsource.atem.impl.MetaLogs;
import org.springframework.beans.factory.annotation.Autowired;


public class HandlerLocatorImpl<T, H extends StaticTypeHandler<T>>
{

	@Autowired
	private BeanLocator beanLocator;

	private SortedMap<Class<T>, SortedSet<Bean<H>>> sortedTypeHandlerBeans;

	private void addBean(Bean<H> bean, Class<T> modelType)
	{
		SortedSet<Bean<H>> SortedSet = sortedTypeHandlerBeans.get(modelType);
		if (SortedSet == null)
		{
			SortedSet = new TreeSet<Bean<H>>(new OrderComparator());
			sortedTypeHandlerBeans.put(modelType, SortedSet);
		}
		SortedSet.add(bean);
	}

	/**
	 * @param exception
	 * @return true - if exception was handled
	 */
	public H getHandler(T t)
	{
		if (sortedTypeHandlerBeans == null)
		{
			initialize();
		}

		for (Map.Entry<Class<T>, SortedSet<Bean<H>>> handler : sortedTypeHandlerBeans.entrySet())
		{

			if (handler.getKey().isInstance(t))
			{
				for (Bean<H> handlerBean : handler.getValue())
				{
					H handlerInstance = handlerBean.get();
					if (handlerInstance.handles(t))
					{
						return handlerInstance;
					}
				}
			}

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void initialize()
	{
		Class<H> handlerClass = ReflectionUtils.getActualTypeParameters(getClass(), HandlerLocatorImpl.class)[1];
		Set<Bean<H>> handlers = beanLocator.getBeans(handlerClass);
		sortedTypeHandlerBeans = new TreeMap<Class<T>, SortedSet<Bean<H>>>(new ClassComparator());
		for (Bean<H> bean : handlers)
		{
			if (!bean.isScopedTarget())
			{
				H instance = bean.get();
				if (instance instanceof DynamicTypeHandler)
				{
					for (Class modelType : ((DynamicTypeHandler) instance).getHandledClasses())
					{
						addBean(bean, modelType);
					}
				}
				else
				{
					Class<T> modelType =
						ReflectionUtils.getActualTypeParameter(instance.getClass(), StaticTypeHandler.class);
					addBean(bean, modelType);
				}
			}
		}
		StringBuilder builder = new StringBuilder();
		builder.append("order of handlers :\n");
		for (Map.Entry<Class<T>, SortedSet<Bean<H>>> entry : sortedTypeHandlerBeans.entrySet())
		{
			builder.append(entry.getKey());
			builder.append(" - ");
			for (Bean<H> bean : entry.getValue())
			{
				builder.append(bean.getBeanName());
				builder.append("/");
			}
			builder.append("\n");
		}
		MetaLogs.LOG.info(builder.toString());
	}

	private final class ClassComparator implements Comparator<Class>
	{
		@Override
		public int compare(Class exceptionType1, Class exceptionType2)
		{
			// we want the order to be reversed/descending
			int compareByHierachyLevel = ReflectionUtils.compareByCommonHierachy(exceptionType1, exceptionType2);
			if (compareByHierachyLevel == 0)
			{
				// not in the same hierachy
				return exceptionType1.getName().compareTo(exceptionType2.getName());
			}
			else
			{
				return compareByHierachyLevel;
			}
		}
	}

	public class OrderComparator implements Comparator<Bean<?>>
	{

		@Override
		public int compare(Bean<?> o1, Bean<?> o2)
		{
			Order order1 = o1.get().getClass().getAnnotation(Order.class);
			Order order2 = o2.get().getClass().getAnnotation(Order.class);
			int position1 = order1 == null ? 0 : order1.value();
			int position2 = order2 == null ? 0 : order2.value();
			if (position1 != position2)
			{
				return position1 - position2;
			}
			else
			{
				return o1.getBeanName().compareTo(o2.getBeanName());
			}
		}

	}
}
