/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.AbstractMetaDataRepository;
import org.atemsource.atem.impl.infrastructure.BeanCreator;
import org.atemsource.atem.impl.pojo.attribute.AttributeFactory;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.atemsource.atem.spi.EntityTypeSubrepository;
import org.springframework.beans.factory.annotation.Autowired;


public class PojoEntityTypeRepository extends AbstractMetaDataRepository<Object> implements
	EntityTypeSubrepository<Object>
{
	private List<AttributeFactory> attributeFactories = new ArrayList<AttributeFactory>();

	@Autowired
	private BeanCreator beanCreator;

	private Class entityClass;

	private Class<? extends AbstractEntityType> entityTypeClass;

	private boolean fieldAccess = false;

	private String includedPackage;

	protected Set<String> nonAvailableEntityNames = new HashSet<String>();

	private List<AbstractEntityType> uninitializedEntityTypes = new ArrayList<AbstractEntityType>();

	protected Attribute addAttribute(AbstractEntityType entityType,
		org.atemsource.atem.impl.pojo.attribute.PropertyDescriptor propertyDescriptor)
	{
		Attribute attribute = null;
		for (AttributeFactory attributeFactory : attributeFactories)
		{
			if (attributeFactory.canCreate(propertyDescriptor, entityTypeCreationContext))
			{
				attribute = attributeFactory.createAttribute(entityType, propertyDescriptor, entityTypeCreationContext);
				break;
			}
		}
		return attribute;
	}

	protected void addAttributes(AbstractEntityType entityType)
	{
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (org.atemsource.atem.impl.pojo.attribute.PropertyDescriptor propertyDescriptor : org.atemsource.atem.impl.pojo.attribute.PropertyDescriptor
			.getPropertyDescriptors(entityType.getEntityClass(), fieldAccess))
		{

			Attribute attribute = addAttribute(entityType, propertyDescriptor);
			if (attribute != null)
			{
				attributes.add(attribute);
			}

		}

		entityType.setAttributes(attributes);
	}

	@Override
	public void afterFirstInitialization(EntityTypeRepository entityTypeRepositoryImpl)
	{
		initializeReferences();
	}

	@Override
	public void afterInitialization()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(EntityType entityType)
	{
		if (entityTypeClass.isInstance(entityType))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	protected synchronized EntityType createEntityType(final Class clazz)
	{
		AbstractEntityType entityType;
		entityType = beanCreator.create(entityTypeClass);
		entityType.setEntityClass(clazz);
		entityType.setCode(clazz.getName());
		nameToEntityTypes.put(clazz.getName(), entityType);
		classToEntityTypes.put(clazz, entityType);
		entityTypes.add(entityType);
		uninitializedEntityTypes.add(entityType);
		initializeReferences();
		return entityType;
	}

	protected EntityType createEntityType(String entityName)
	{
		Class clazz;
		try
		{
			clazz = Class.forName(entityName);
		}
		catch (ClassNotFoundException e)
		{
			return null;
		}
		if (!isAvailable(clazz))
		{
			return null;
		}
		return createEntityType(clazz);
	}

	public List<AttributeFactory> getAttributeFactories()
	{
		return attributeFactories;
	}

	public Class getEntityClass()
	{
		return entityClass;
	}

	@Override
	public EntityType getEntityType(Class clazz)
	{
		if (isAvailable(clazz))
		{
			return null;
		}
		EntityType entityType = super.getEntityType(clazz);
		if (entityType == null)
		{
			entityType = createEntityType(clazz);

		}
		return entityType;
	}

	@Override
	public EntityType<Object> getEntityType(Object entity)
	{
		if (entity == null)
		{
			return null;
		}
		else
		{
			return getEntityType(entity.getClass());
		}
	}

	@Override
	public EntityType getEntityType(String entityName)
	{
		if (nonAvailableEntityNames.contains(entityName))
		{
			return null;
		}
		EntityType entityType = super.getEntityType(entityName);
		// TODO why was this here
		// if (entityType == null)
		// {
		// entityType = createEntityType(entityName);
		// }
		if (entityType == null)
		{
			nonAvailableEntityNames.add(entityName);
		}
		return entityType;
	}

	@Override
	public EntityType getEntityTypeReference(Class clazz)
	{
		if (isAvailable(clazz))
		{
			return null;
		}
		AbstractEntityType entityType = (AbstractEntityType) this.classToEntityTypes.get(clazz);
		if (entityType == null)
		{
			entityType = beanCreator.create(entityTypeClass);
			entityType.setEntityClass(clazz);
			entityType.setCode(clazz.getName());
			nameToEntityTypes.put(clazz.getName(), entityType);
			classToEntityTypes.put(clazz, entityType);
			entityTypes.add(entityType);
			uninitializedEntityTypes.add(entityType);
		}
		return entityType;
	}

	@Override
	public EntityType getEntityTypeReference(String entityName)
	{
		Class clazz;
		try
		{
			clazz = Class.forName(entityName);
		}
		catch (ClassNotFoundException e)
		{
			return null;
		}
		return getEntityTypeReference(clazz);
	}

	public String getIncludedPackage()
	{
		return includedPackage;
	}

	@Override
	public void initialize(EntityTypeCreationContext entityTypeCreationContext)
	{
		this.entityTypeCreationContext = entityTypeCreationContext;

		initializeLookups();
	}

	protected void initializeEntityType(AbstractEntityType entityType)
	{
		Class clazz = entityType.getEntityClass();
		 Class superclass = clazz.getSuperclass();
		if (clazz.isInterface() && clazz.getInterfaces().length==1) {
			// TODO workaround for interface inheritance hierachy (Attribute)
			superclass=clazz.getInterfaces()[0];
		}
		if (superclass != null && !superclass.equals(Object.class))
		{
			final EntityType superType = getEntityTypeReference(superclass);
			entityType.setSuperEntityType(superType);
			// TODO this only works for AbstractEntityType but actually should work for all.
			if (superType instanceof AbstractEntityType)
			{
				((AbstractEntityType) superType).addSubEntityType(entityType);
			}
		}
		addAttributes(entityType);
		attacheServicesToEntityType(entityType);
		entityType.initializeIncomingAssociations(entityTypeCreationContext);
		entityTypeCreationContext.lazilyInitialized(entityType);
	}

	private void initializeReferences()
	{
		while (uninitializedEntityTypes.size() > 0)
		{
			Set<AbstractEntityType> copy = new HashSet<AbstractEntityType>(uninitializedEntityTypes);
			uninitializedEntityTypes.clear();
			for (Iterator<AbstractEntityType> iterator = copy.iterator(); iterator.hasNext();)
			{
				AbstractEntityType uninitializedEntityType = iterator.next();
				initializeEntityType(uninitializedEntityType);
			}
		}
	}

	protected boolean isAvailable(Class clazz)
	{
		return (entityClass != null && !entityClass.isAssignableFrom(clazz))
			|| (includedPackage != null && !clazz.getPackage().getName().startsWith(includedPackage));
	}

	private boolean isAvailable(String entityName)
	{
		AbstractEntityType<Object> abstractEntityType = nameToEntityTypes.get(entityName);
		if (abstractEntityType != null)
		{
			return true;
		}
		try
		{
			Class.forName(entityName);
			return true;
		}
		catch (ClassNotFoundException e)
		{
			return false;
		}
	}

	public void setAttributeFactories(List<AttributeFactory> attributeFactories)
	{
		this.attributeFactories = attributeFactories;
	}

	public void setEntityClass(Class entityClass)
	{
		this.entityClass = entityClass;
	}

	public void setEntityTypeClass(Class<? extends AbstractEntityType> entityTypeClass)
	{
		this.entityTypeClass = entityTypeClass;
	}

	public void setFieldAccess(boolean fieldAccess)
	{
		this.fieldAccess = fieldAccess;
	}

	public void setIncludedPackage(String includedPackage)
	{
		this.includedPackage = includedPackage;
	}

}
