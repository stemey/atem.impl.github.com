/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.extension.EntityTypePostProcessor;
import org.atemsource.atem.api.extension.EntityTypeRepositoryPostProcessor;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.IncomingRelation;
import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.primitive.PrimitiveTypeFactory;
import org.atemsource.atem.impl.infrastructure.BeanCreator;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.atemsource.atem.spi.EntityTypeRepositoryListener;
import org.atemsource.atem.spi.EntityTypeSubrepository;
import org.atemsource.atem.spi.Phase;
import org.atemsource.atem.spi.PhaseEvent;
import org.springframework.beans.factory.annotation.Autowired;


public class EntityTypeRepositoryImpl implements EntityTypeRepository, EntityTypeCreationContext
{

	@Autowired
	private BeanCreator beanCreator;

	private Collection<EntityTypePostProcessor> entityTypePostProcessors;

	private Collection<EntityTypeRepositoryPostProcessor> entityTypeRepositoryPostProcessors;

	List<EntityTypeSubrepository> entityTypeSubrepositories;

	private final List<EntityTypeRepositoryListener> listeners = new ArrayList<EntityTypeRepositoryListener>();

	@Autowired
	private PrimitiveTypeFactory primitiveTypeFactory;

	private boolean secondPhase;

	@Override
	public void addIncomingAssociation(EntityType entityType, IncomingRelation<?, ?> incomingRelation)
	{
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			if (entityTypeSubrepository.contains(entityType))
			{
				entityTypeSubrepository.addIncomingAssociation(entityType, incomingRelation);
			}
		}
	}

	@Override
	public void addListener(EntityTypeRepositoryListener entityTypeRepositoryListener)
	{
		this.listeners.add(entityTypeRepositoryListener);
	}

	@Override
	public void addMetaAttribute(EntityType entityType, Attribute<?, ?> metaAttribute)
	{
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			if (entityTypeSubrepository.contains(entityType))
			{
				entityTypeSubrepository.addMetaAttribute(entityType, metaAttribute);
			}
		}
	}

	@Override
	public EntityType getEntityType(Class clazz)
	{
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			EntityType entityType = entityTypeSubrepository.getEntityType(clazz);
			if (entityType != null)
			{
				return entityType;
			}
		}
		return null;
	}

	@Override
	public <J> EntityType<J> getEntityType(J entity)
	{
		for (EntityTypeSubrepository subrepository : entityTypeSubrepositories)
		{
			EntityType entityType = subrepository.getEntityType(entity);
			if (entityType != null)
			{
				return entityType;
			}
		}
		return null;
	}

	@Override
	public EntityType getEntityType(String typeCode)
	{
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			EntityType entityType = entityTypeSubrepository.getEntityType(typeCode);
			if (entityType != null)
			{
				return entityType;
			}
		}
		return null;
	}

	@Override
	public EntityType getEntityTypeReference(Class clazz)
	{
		EntityType entityType = null;
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			entityType = entityTypeSubrepository.getEntityTypeReference(clazz);
			if (entityType != null)
			{
				break;
			}

		}
		if (entityType == null)
		{
			return null;
		}
		else if (secondPhase)
		{
			performInitializationBeforeSecondPhase();
		}
		return entityType;
	}

	@Override
	public EntityType getEntityTypeReference(String typeCode)
	{
		EntityType entityType = null;
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			entityType = entityTypeSubrepository.getEntityTypeReference(typeCode);
			if (entityType != null)
			{
				break;
			}
		}
		if (entityType == null)
		{
			return null;
		}
		else if (secondPhase)
		{
			performLazyRepositoryInit();
		}
		return entityType;

	}

	public Collection<EntityTypeRepositoryPostProcessor> getEntityTypeRepositoryPostProcessors()
	{
		return entityTypeRepositoryPostProcessors;
	}

	@Override
	public Collection<EntityType<?>> getEntityTypes()
	{
		List<EntityType<?>> entityTypes = new ArrayList<EntityType<?>>();
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			entityTypes.addAll(entityTypeSubrepository.getEntityTypes());
		}
		return entityTypes;
	}

	@Override
	public <J> Type<J> getType(Class<J> clazz)
	{
		PrimitiveType primitiveType = primitiveTypeFactory.getPrimitiveType(clazz);
		if (primitiveType != null)
		{
			return primitiveType;
		}
		else
		{
			return getEntityType(clazz);
		}
	}

	@Override
	public <J> Type<J> getType(J entity)
	{
		PrimitiveType primitiveType = primitiveTypeFactory.getPrimitiveType(entity.getClass());
		if (primitiveType == null)
		{
			return getEntityType(entity);
		}
		else
		{
			return primitiveType;
		}
	}

	@Override
	public Type getTypeReference(Class clazz)
	{
		PrimitiveType primitiveType = primitiveTypeFactory.getPrimitiveType(clazz);
		if (primitiveType != null)
		{
			return primitiveType;
		}
		else
		{

			return getEntityTypeReference(clazz);
		}
	}

	@Override
	public boolean hasEntityTypeReference(Class entityClass)
	{

		if (primitiveTypeFactory.getPrimitiveType(entityClass) != null)
		{
			return true;
		}
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			if (entityTypeSubrepository.hasEntityTypeReference(entityClass))
			{
				return true;
			}
		}
		return false;
	}

	public void init()
	{
		entityTypePostProcessors = beanCreator.getBeans(EntityTypePostProcessor.class);
		entityTypeRepositoryPostProcessors = beanCreator.getBeans(EntityTypeRepositoryPostProcessor.class);
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			entityTypeSubrepository.initialize(this);
		}
		onPhase(Phase.ENTITY_TYPES_INITIALIZED);
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			entityTypeSubrepository.afterFirstInitialization(this);
		}
		for (EntityTypeRepositoryPostProcessor entityTypeRepositoryPostProcessor : entityTypeRepositoryPostProcessors)
		{
			entityTypeRepositoryPostProcessor.initialize(this);
		}
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			entityTypeSubrepository.initializeIncomingAssociations();
		}
		secondPhase = true;
		for (EntityType entityType : getEntityTypes())
		{
			for (EntityTypePostProcessor entityTypePostProcessor : entityTypePostProcessors)
			{
				entityTypePostProcessor.postProcessEntityType(this, entityType);
			}
		}
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			entityTypeSubrepository.afterInitialization();
		}
	}

	@Override
	public void lazilyInitialized(EntityType entityType)
	{
		for (EntityTypePostProcessor entityTypePostProcessor : entityTypePostProcessors)
		{
			entityTypePostProcessor.postProcessEntityType(this, entityType);
		}
	}

	@Override
	public void onPhase(Phase phase)
	{
		for (EntityTypeRepositoryListener listener : listeners)
		{
			listener.onEvent(new PhaseEvent(this, phase));
		}
	}

	private void performInitializationBeforeSecondPhase()
	{
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			entityTypeSubrepository.performLazyRepositoryInit(this);
		}
	}

	private void performLazyRepositoryInit()
	{
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories)
		{
			entityTypeSubrepository.performLazyRepositoryInit(this);
		}
	}

	@Override
	public void registerType(Class<?> clazz, PrimitiveType primitiveType)
	{
		primitiveTypeFactory.registerType(clazz, primitiveType);
	}

	public void setEntityTypeRepositoryPostProcessors(
		Collection<EntityTypeRepositoryPostProcessor> entityTypeRepositoryPostProcessors)
	{
		this.entityTypeRepositoryPostProcessors = entityTypeRepositoryPostProcessors;
	}

	public void setRepositories(List<EntityTypeSubrepository> entityTypeSubrepositories)
	{
		this.entityTypeSubrepositories = entityTypeSubrepositories;
	}

}
