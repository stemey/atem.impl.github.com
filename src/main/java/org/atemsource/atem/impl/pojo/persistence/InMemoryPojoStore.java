/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.atemsource.atem.api.BeanLocator;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.service.AttributeQuery;
import org.atemsource.atem.api.service.FindByAttributeService;
import org.atemsource.atem.api.service.FindByIdService;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.service.PersistenceService;
import org.atemsource.atem.api.service.SingleAttributeQuery;
import org.atemsource.atem.api.type.EntityType;
import org.springframework.beans.factory.annotation.Autowired;


public class InMemoryPojoStore implements FindByAttributeService, PersistenceService, FindByIdService
{

	private static final String FIND_BY_ATTRIBUTE = "findByAttribute";

	private static final String FIND_BY_ID = "findById";

	@Inject
	private BeanLocator beanLocator;

	private final Collection<Object> entities = new HashSet<Object>();

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	private final Map<String, SingleViewIndex> singleViewIndexes = new HashMap<String, SingleViewIndex>();

	private List<Class> supportedClasses;

	private final Map<String, ViewIndex> viewIndexes = new HashMap<String, ViewIndex>();

	public void clearAssociatedEntities(Object entity, CollectionAttribute collectionAssociationAttribute)
	{
		throw new UnsupportedOperationException("readonly");
	}

	public Set<Object> find(String view, Object... parameters)
	{
		return viewIndexes.get(view).find(parameters);
	}

	@Override
	public <E> E findById(EntityType<E> entityType, Serializable id)
	{
		return (E) singleViewIndexes.get(FIND_BY_ID).find(new Object[]{entityType, id});
	}

	public Object findSingle(String view, Object... parameters)
	{
		return singleViewIndexes.get(view).find(parameters);
	}

	public Object findSingleByAttribute(Object targetEntity, Attribute<?, ?> attribute)
	{
		return singleViewIndexes.get(FIND_BY_ATTRIBUTE).find(new Object[]{attribute, targetEntity});
	}

	public <T> Collection<T> getEntities(Class<T> clazz)
	{
		List<T> entities = new ArrayList<T>();
		for (Object entity : entities)
		{
			if (clazz.isInstance(entity))
			{
				entities.add((T) entity);
			}
		}
		return entities;
	}

	public List<Class> getSupportedClasses()
	{
		return supportedClasses;
	}

	@PostConstruct
	public void initialize()
	{
		singleViewIndexes.put(FIND_BY_ATTRIBUTE,
			new SingleViewIndex(beanLocator.getInstance(SingleAttributeViewCreator.class)));
		singleViewIndexes.put(FIND_BY_ID, new SingleViewIndex(beanLocator.getInstance(FindByIdViewCreator.class)));
	}

	@Override
	public <E> Serializable insert(EntityType<E> entityType, E entity)
	{
		Serializable id = entityType.getService(IdentityService.class).getId(entityType, entity);
		entities.add(entity);
		for (SingleViewIndex index : singleViewIndexes.values())
		{
			index.insert(entity);
		}
		for (ViewIndex index : viewIndexes.values())
		{
			index.insert(entity);
		}
		return id;
	}

	public boolean isEqual(CollectionAttribute collectionAssociationAttribute, Object entity, Object other)
	{
		throw new UnsupportedOperationException("not implmeneted yet");
	}

	@Override
	public <E> boolean isPersistent(EntityType<E> entityType, E entity)
	{
		return entities.contains(entity);
	}

	@Override
	public AttributeQuery prepareQuery(EntityType<?> entityType, Attribute<?, ?> attribute)
	{
		return new PojoAttributeQuery(attribute, this);
	}

	@Override
	public SingleAttributeQuery prepareSingleQuery(EntityType<?> entityType, Attribute<?, ?> attribute)
	{
		return new SinglePojoAttributeQuery(attribute, this);
	}

	public void setSupportedClasses(List<Class> supportedClasses)
	{
		this.supportedClasses = supportedClasses;
	}
}
