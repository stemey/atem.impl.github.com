/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.service.AttributeQuery;
import org.atemsource.atem.api.service.FindByAttributeService;
import org.atemsource.atem.api.service.FindByTypedIdService;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.service.PersistenceService;
import org.atemsource.atem.api.service.SingleAttributeQuery;
import org.atemsource.atem.api.type.EntityType;
import org.springframework.beans.factory.annotation.Autowired;


public class InMemoryPojoRepository implements FindByAttributeService, PersistenceService, FindByTypedIdService
{

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	private List<Class> supportedClasses;

	private Map<TypedId, Object> typedIds = new HashMap<TypedId, Object>();

	private Map<Attribute, Map<Object, Object>> singleIndexes = new HashMap<Attribute, Map<Object, Object>>();

	private boolean createIndex;

	public void clearAssociatedEntities(Object entity, CollectionAttribute collectionAssociationAttribute)
	{
		throw new UnsupportedOperationException("readonly");
	}

	@Override
	public Object findByTypedId(EntityType<?> entityType, Serializable id)
	{
		return typedIds.get(new TypedId(entityType.getCode(), id));
	}

	public Object findSingleByAttribute(Object targetEntity, Attribute<?, ?> attribute)
	{
		Map<Object, Object> index = null;
		if (singleIndexes != null)
		{
			index = singleIndexes.get(attribute);
		}
		if (index == null)
		{
			if (createIndex)
			{

				index = new HashMap<Object, Object>();
				singleIndexes.put(attribute, index);
			}
			Object entities = new HashSet<Object>();
			for (Object entity : typedIds.values())
			{
				EntityType<Object> entityType = entityTypeRepository.getEntityType(entity);
				if (attribute.getEntityType().isAssignableFrom(entity))
				{
					if (attribute instanceof SingleAttribute<?>)
					{
						Object aTargetEntity = attribute.getValue(entity);
						if (targetEntity.equals(aTargetEntity))
						{
							if (createIndex)
							{

								index.put(targetEntity, entity);
							}
							return entity;
						}
					}
				}
			}

			return null;
		}
		else
		{
			return index.get(targetEntity);
		}
	}

	public <T> Collection<T> getEntities(Class<T> clazz)
	{
		List<T> entities = new ArrayList<T>();
		for (Object entity : typedIds.values())
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

	@Override
	public void insert(Object entity)
	{
		EntityType<?> entityType = entityTypeRepository.getEntityType(entity);
		Serializable id = entityType.getService(IdentityService.class).getId(entityType, entity);
		typedIds.put(new TypedId(entityType.getCode(), id), entity);
	}

	public boolean isCreateIndex()
	{
		return createIndex;
	}

	public boolean isEqual(CollectionAttribute collectionAssociationAttribute, Object entity, Object other)
	{
		throw new UnsupportedOperationException("not implmeneted yet");
	}

	@Override
	public boolean isPersistent(Object entity)
	{
		EntityType<?> entityType = entityTypeRepository.getEntityType(entity);
		Serializable id = entityType.getService(IdentityService.class).getId(entityType, entity);
		return typedIds.get(new TypedId(entityType.getCode(), id)) != null;
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

	public void setCreateIndex(boolean createIndex)
	{
		this.createIndex = createIndex;
	}

	public void setSupportedClasses(List<Class> supportedClasses)
	{
		this.supportedClasses = supportedClasses;
	}

	private static class TypedId implements Serializable
	{
		private String typeCode;

		private Serializable id;

		public TypedId(String typeCode, Serializable id)
		{
			super();
			this.typeCode = typeCode;
			this.id = id;
		}

		public Serializable getId()
		{
			return id;
		}

		public String getTypeCode()
		{
			return typeCode;
		}
	}
}
