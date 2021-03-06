/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.extension.EntityTypeServiceFactory;
import org.atemsource.atem.api.infrastructure.exception.BusinessException;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.IncomingRelation;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.atemsource.atem.spi.EntityTypeSubrepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstractMetaDataRepository<J> implements
		EntityTypeSubrepository<J>, ApplicationContextAware {

	protected ApplicationContext applicationContext;

	protected Map<Class<J>, AbstractEntityType<J>> classToEntityTypes = new HashMap<Class<J>, AbstractEntityType<J>>();

	protected EntityTypeCreationContext entityTypeCreationContext;

	protected Set<AbstractEntityType<J>> entityTypes = new HashSet<AbstractEntityType<J>>();

	private List<EntityTypeServiceFactory> entityTypeServiceFactories;

	private Map<Class<?>, Object> entityTypeServices;

	private Collection<Object> services;
	
	public <T> T getService(Class<T> serviceClass) {
		return (T) entityTypeServices.get(serviceClass);
	}

	public Collection<Object> getServices() {
		return services;
	}

	public void setServices(Collection<Object> services) {
		this.services = services;
	}

	protected Map<String, AbstractEntityType<J>> nameToEntityTypes = new HashMap<String, AbstractEntityType<J>>();

	@Override
	public void addIncomingAssociation(EntityType<J> entityType,
			IncomingRelation<?, ?> incomingRelation) {
		((AbstractEntityType) entityType)
				.addIncomingAssociation(incomingRelation);
	}

	@Override
	public void addMetaAttribute(EntityType<J> entityType,
			Attribute<?, ?> metaAttribute) {
		((AbstractEntityType) entityType).addMetaAttribute(metaAttribute);
	}

	public void addServices(final AbstractEntityType entityType,
			Class serviceClass, Object service) {
		for (Class<?> serviceInterface : serviceClass.getInterfaces()) {
			entityType.addService(serviceInterface, service);
			addServices(entityType, serviceInterface, service);
		}
	}

	@SuppressWarnings("unchecked")
	protected void attacheServicesToEntityType(
			final AbstractEntityType entityType) {
		if (entityTypeServices != null) {
			for (Map.Entry<Class<?>, Object> entry : entityTypeServices
					.entrySet()) {
				if (!entry.getKey().isInstance(entry.getValue())) {
					throw new IllegalStateException(
							"the service does not implement "
									+ entry.getKey().getName());
				}
				entityType.addService(entry.getKey(), entry.getValue());
			}
		}
		if (entityTypeServiceFactories != null) {
			for (EntityTypeServiceFactory factory : entityTypeServiceFactories) {
				entityType.addService(factory.getServiceClass(entityType),
						factory.createService(entityType));
			}
		}
		if (services != null) {
			for (Object service : services) {
				addServices(entityType, service.getClass(), service);
			}
		}
	}

	@Override
	public boolean contains(EntityType<J> entityType) {
		return entityTypes.contains(entityType);
	}

	@Override
	public EntityType<J> getEntityType(final Class clazz) {
		return classToEntityTypes.get(clazz);
	}

	@Override
	public EntityType<J> getEntityType(final String entityName) {
		// TODO commerce entittype code is not the same as hibernate entity name
		// that is why we try both here
		EntityType entityType = nameToEntityTypes.get(entityName);
		return entityType;
	}

	@Override
	public EntityType<J> getEntityTypeReference(Class<J> propertyType) {
		return classToEntityTypes.get(propertyType);
	}

	@Override
	public EntityType<J> getEntityTypeReference(String entityName) {
		return nameToEntityTypes.get(entityName);
	}

	@Override
	public Collection<AbstractEntityType<J>> getEntityTypes() {
		return entityTypes;
	}

	public List<EntityTypeServiceFactory> getEntityTypeServiceFactories() {
		return entityTypeServiceFactories;
	}

	public Map<Class<?>, Object> getEntityTypeServices() {
		return entityTypeServices;
	}

	@Override
	public boolean hasEntityTypeReference(Class entityClass) {
		return classToEntityTypes.containsKey(entityClass);
	}

	protected void initializeEntityTypeHierachy(AbstractEntityType entityType,
			String superTypeCode) {
		AbstractEntityType superEntityType = nameToEntityTypes
				.get(superTypeCode);
		if (superEntityType != null) {
			entityType.setSuperEntityType(superEntityType);
			superEntityType.addSubEntityType(entityType);
		}
	}

	@Override
	public void initializeIncomingAssociations() {
		for (AbstractEntityType entityType : nameToEntityTypes.values()) {
			entityType
					.initializeIncomingAssociations(entityTypeCreationContext);
		}
	}

	protected void initializeLookups() {
		entityTypes.addAll(nameToEntityTypes.values());
		for (AbstractEntityType entityType : entityTypes) {
			classToEntityTypes.put(entityType.getEntityClass(), entityType);
		}
	}

	@Override
	public void performLazyRepositoryInit(EntityTypeCreationContext ctx) {

	}

	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setEntityTypeServiceFactories(
			List<EntityTypeServiceFactory> entityTypeServiceFactories) {
		this.entityTypeServiceFactories = entityTypeServiceFactories;
	}

	public void setEntityTypeServices(Map<Class<?>, Object> entityTypeServices) {
		this.entityTypeServices = entityTypeServices;
	}

	public void clear() {
		nameToEntityTypes.clear();
		entityTypes.clear();
		classToEntityTypes.clear();
	}
	
	public void remove(String typeCode) {
		AbstractEntityType<J> type = nameToEntityTypes.get(typeCode);
		entityTypes.remove(type);
		nameToEntityTypes.remove(typeCode);
		
	}
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
