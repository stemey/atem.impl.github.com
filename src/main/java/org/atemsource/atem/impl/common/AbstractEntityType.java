/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;
import org.atemsource.atem.api.service.AttributeQuery;
import org.atemsource.atem.api.service.FindByAttributeService;
import org.atemsource.atem.api.service.SingleAttributeQuery;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.view.AttributeVisitor;
import org.atemsource.atem.api.view.View;
import org.atemsource.atem.api.view.ViewVisitor;
import org.atemsource.atem.impl.common.attribute.AbstractAttribute;
import org.atemsource.atem.impl.infrastructure.BeanCreator;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractEntityType<J> implements EntityType<J>
{

	private boolean abstractType;

	private Map<String, Attribute> attributeCodes = new HashMap<String, Attribute>();

	private List<Attribute> attributes = new ArrayList<Attribute>();

	@Autowired
	private BeanCreator beanCreator;

	private String code;

	private Class entityClass;

	@Resource
	protected EntityTypeRepository entityTypeRepository;

	private Collection<Attribute> incomingAssociations = new ArrayList<Attribute>();

	private Map<String, Attribute> metaAttributes = new HashMap<String, Attribute>();

	private Set<EntityType<?>> selfAndSubTypes = new HashSet<EntityType<?>>();

	private final Map<Class, Object> serviceMap = new HashMap<Class, Object>();

	private Set<EntityType> subEntityTypes = new HashSet<EntityType>();

	private EntityType superEntityType;

	protected AbstractEntityType()
	{
		super();
		selfAndSubTypes.add(this);
	}

	public void addAttribute(Attribute attribute)
	{
		if (getDeclaredAttribute(attribute.getCode()) == null)
		{
			attributes.add(attribute);
			attributeCodes.put(attribute.getCode(), attribute);
		}
		else
		{
			throw new IllegalStateException("attribute " + getCode() + "." + attribute.getCode() + " already exists");
		}
	}

	public void addIncomingAssociation(Attribute<?, ?> attribute)
	{
		incomingAssociations.add(attribute);
	}

	public void addMetaAttribute(Attribute<?, ?> attribute)
	{
		metaAttributes.put(attribute.getCode(), attribute);
	}

	public void addService(final Class key, final Object service)
	{
		serviceMap.put(key, service);
	}

	public void addSubEntityType(final EntityType entityType)
	{
		subEntityTypes.add(entityType);
		selfAndSubTypes.add(entityType);
	}

	@Override
	public Object[] createArray(final int length)
	{
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public J createEntity() throws TechnicalException
	{
		try
		{
			final Constructor constructor = entityClass.getDeclaredConstructor(new Class[0]);
			constructor.setAccessible(true);
			return (J) constructor.newInstance(new Object[0]);
		}
		catch (InstantiationException e)
		{
			throw new TechnicalException("cannot create entity", e);
		}
		catch (IllegalAccessException e)
		{
			throw new TechnicalException("cannot create entity", e);
		}
		catch (SecurityException e)
		{
			throw new TechnicalException("cannot create entity", e);
		}
		catch (NoSuchMethodException e)
		{
			throw new TechnicalException("cannot create entity", e);
		}
		catch (IllegalArgumentException e)
		{
			throw new TechnicalException("cannot create entity", e);
		}
		catch (InvocationTargetException e)
		{
			throw new TechnicalException("cannot create entity", e);
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof EntityType<?>)
		{
			return getCode().equals(((EntityType<?>) obj).getCode());
		}
		else
		{
			return false;
		}
	}

	@Override
	public Set<EntityType> getAllSubEntityTypes()
	{
		return subEntityTypes;
	}

	@Override
	public Attribute getAttribute(final String code)
	{
		Attribute attribute = attributeCodes.get(code);
		if (attribute == null && getSuperEntityType() != null)
		{
			return getSuperEntityType().getAttribute(code);
		}
		else
		{
			return attribute;
		}
	}

	@Override
	public List<Attribute> getAttributes()
	{
		List<Attribute> allAttributes = new ArrayList<Attribute>();
		allAttributes.addAll(this.attributes);
		if (getSuperEntityType() != null)
		{
			allAttributes.addAll(getSuperEntityType().getAttributes());
		}
		return allAttributes;
	}

	@Override
	public String getCode()
	{
		return code;
	}

	@Override
	public Attribute getDeclaredAttribute(final String code)
	{
		return attributeCodes.get(code);
	}

	@Override
	public List<Attribute> getDeclaredAttributes()
	{
		return attributes;
	}

	@Override
	public Class getEntityClass()
	{
		return entityClass;
	}

	@Override
	public Attribute getIncomingAssociation(String sourceTypeCode)
	{
		EntityType<?> source = entityTypeRepository.getEntityType(sourceTypeCode);
		for (Attribute<?, ?> relationAttribute : incomingAssociations)
		{
			if (relationAttribute.getTargetType().isAssignableFrom(source))
			{
				return relationAttribute;
			}
		}
		return null;
	}

	@Override
	public Attribute getIncomingAssociation(String sourceTypeCode, String attributeCode)
	{
		EntityType<?> source = entityTypeRepository.getEntityType(sourceTypeCode);
		for (Attribute<?, ?> relationAttribute : incomingAssociations)
		{
			if (relationAttribute.getCode().equals(attributeCode)
				&& relationAttribute.getTargetType().isAssignableFrom(source))
			{
				return relationAttribute;
			}
		}
		return null;
	}

	@Override
	public Attribute getMetaAttribute(final String code)
	{
		Attribute attribute = metaAttributes.get(code);
		return attribute;
	}

	public Collection<Attribute> getMetaAttributes()
	{
		return metaAttributes.values();
	}

	@Override
	public Set<EntityType<?>> getSelfAndAllSubEntityTypes()
	{
		return selfAndSubTypes;
	}

	@Override
	public <T> T getService(final Class<T> serviceInterface)
	{
		return (T) serviceMap.get(serviceInterface);
	}

	public Map<Class, Object> getServiceMap()
	{
		return serviceMap;
	}

	@Override
	public Set<EntityType> getSubEntityTypes(final boolean includeAbstract)
	{
		return subEntityTypes;
	}

	@Override
	public EntityType getSuperEntityType()
	{
		return superEntityType;
	}

	@Override
	public boolean hasAttribute(final String code)
	{
		return attributeCodes.containsKey(code);
	}

	@Override
	public int hashCode()
	{
		if (getCode() == null)
		{
			return super.hashCode();
		}
		else
		{
			return getCode().hashCode();
		}
	}

	public void initializeIncomingAssociations(EntityTypeCreationContext context)
	{
		FindByAttributeService findByAttributeService = getService(FindByAttributeService.class);
		for (Attribute<?, ?> attribute : attributes)
		{
			// TODO add more relation attributes for reverse incoming
			// associations
			if (attribute.getTargetType() instanceof EntityType<?> && attribute.getTargetCardinality() != null)
			{
				EntityType<?> entityType = (EntityType<?>) ((Attribute) attribute).getTargetType();
				String incomingCode = attribute.getEntityType().getCode() + ":" + attribute.getCode();
				switch (attribute.getTargetCardinality())
				{
					case MANY:
					case ZERO_TO_MANY:
						IncomingManyRelation incomingManyRelation = beanCreator.create(IncomingManyRelation.class);
						AttributeQuery query = null;
						if (findByAttributeService != null)
						{
							query = findByAttributeService.prepareQuery(this, attribute);
						}
						incomingManyRelation.setAttributeQuery(query);
						incomingManyRelation.setCode(incomingCode);
						incomingManyRelation.setComposition(false);
						incomingManyRelation.setEntityType((EntityType) attribute.getTargetType());
						incomingManyRelation.setRequired(attribute.isRequired());
						incomingManyRelation.setTargetCardinality(Cardinality.ZERO_TO_MANY);
						incomingManyRelation.setTargetType(attribute.getEntityType());
						((AbstractAttribute) attribute).setIncomingRelation(incomingManyRelation);
						context.addIncomingAssociation(entityType, incomingManyRelation);
					break;
					case ONE:
					case ZERO_TO_ONE:
						IncomingOneRelation incomingOneRelation = beanCreator.create(IncomingOneRelation.class);
						SingleAttributeQuery singleQuery = null;
						if (findByAttributeService != null)
						{
							singleQuery = findByAttributeService.prepareSingleQuery(this, attribute);
						}
						incomingOneRelation.setAttributeQuery(singleQuery);
						incomingOneRelation.setCode(incomingCode);
						incomingOneRelation.setAttribute(attribute);
						((AbstractAttribute) attribute).setIncomingRelation(incomingOneRelation);
						context.addIncomingAssociation(entityType, incomingOneRelation);
				}

			}
		}
	}

	@Override
	public boolean isAbstractType()
	{
		return abstractType;
	}

	@Override
	public boolean isAssignableFrom(final Type<?> type)
	{
		return this.equals(type) || getSubEntityTypes(true).contains(type);
	}

	@Override
	public boolean isEqual(Object entity, Object other)
	{
		if (entity == null && other == null)
		{
			return true;
		}
		else if (entity == null && other != null || entity != null && other == null)
		{
			return false;
		}
		EntityType<Object> entityType = entityTypeRepository.getEntityType(entity);
		EntityType<Object> otherType = entityTypeRepository.getEntityType(other);

		// can be null for dynamic types
		if (entityType != null && otherType != null && !entityType.equals(otherType))
		{
			return false;
		}

		for (Attribute attribute : getDeclaredAttributes())
		{
			if (!attribute.isEqual(entity, other))
			{
				return false;
			}
		}
		if (getSuperEntityType() != null)
		{
			return getSuperEntityType().isEqual(entity, other);
		}
		else
		{

			return true;
		}
	}

	public void setAbstractType(final boolean abstractType)
	{
		this.abstractType = abstractType;
	}

	public void setAttributeCodes(final Map<String, Attribute> attributeCodes)
	{
		this.attributeCodes = attributeCodes;
	}

	public void setAttributes(final List<Attribute> attributes)
	{
		this.attributes = attributes;
		for (Attribute attribute : attributes)
		{
			attributeCodes.put(attribute.getCode(), attribute);
		}
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public void setEntityClass(final Class entityClass)
	{
		this.entityClass = entityClass;
	}

	public void setSelfAndSubTypes(final Set<EntityType<?>> selfAndSubTypes)
	{
		this.selfAndSubTypes = selfAndSubTypes;
	}

	public void setSubEntityTypes(final Set<EntityType> subEntityTypes)
	{
		this.subEntityTypes = subEntityTypes;
	}

	public void setSuperEntityType(final EntityType superEntityType)
	{
		this.superEntityType = superEntityType;
	}

	@Override
	public <C> void visit(ViewVisitor<C> visitor, C context)
	{
		for (Attribute<?, ?> attribute : getDeclaredAttributes())
		{
			if (attribute.getTargetType() instanceof PrimitiveType)
			{
				visitor.visit(context, attribute);
			}
			else
			{
				Type<?> targetType = attribute.getTargetType();
				if (targetType == null)
				{
					throw new IllegalStateException("target type of " + attribute.getCode() + " is null");
				}
				visitor.visit(context, attribute, new AttributeVisitor<C>(visitor, (View) targetType));
			}
		}
		if (superEntityType != null)
		{
			superEntityType.visit(visitor, context);
		}
	}

}
