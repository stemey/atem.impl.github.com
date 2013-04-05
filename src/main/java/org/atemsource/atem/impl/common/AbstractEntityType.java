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
	
	private EntityType<EntityType<J>> metaType;

	public EntityType<EntityType<J>> getMetaType() {
		return metaType;
	}

	public void setMetaType(EntityType<EntityType<J>> metaType) {
		this.metaType = metaType;
	}

	@Resource
	protected EntityTypeRepository entityTypeRepository;

	private final Collection<Attribute> incomingAssociations = new ArrayList<Attribute>();

	private final Map<String, Attribute> metaAttributes = new HashMap<String, Attribute>();

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
			return;// throw new IllegalStateException("attribute " + getCode() +
						// "." + attribute.getCode() +
						// " already exists");
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

	public void addMixin(EntityType<?> mixinType)
	{
		throw new UnsupportedOperationException("mixins are not supported by this type");
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
		Attribute attribute = getDeclaredAttribute(code);
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
		Set<String> attributeCodes = new HashSet<String>();
		attributeCodes.addAll(this.attributeCodes.keySet());
		allAttributes.addAll(getDeclaredAttributes());
		if (getSuperEntityType() != null)
		{
			for (Attribute attribute : getSuperEntityType().getAttributes())
			{
				if (!attributeCodes.contains(attribute.getCode()))
				{
					allAttributes.add(attribute);
				}
			}
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
		if (attribute == null && superEntityType != null)
		{
			return superEntityType.getMetaAttribute(code);
		}
		return attribute;
	}

	@Override
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
	public Set<EntityType> getSubEntityTypes()
	{
		return subEntityTypes;
	}

	@Override
	public Set<EntityType> getSubEntityTypes(final boolean includeAbstract)
	{
		return subEntityTypes;
	}

	@Override
	public EntityType<J> getSuperEntityType()
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
						// ((Attribute<?,?>)attribute).setMetaType((EntityType)entityTypeRepository.getEntityType(incomingManyRelation));
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
						if (attribute instanceof AbstractAttribute)
						{
							// TODO lift this restriction
							((AbstractAttribute) attribute).setIncomingRelation(incomingOneRelation);
							context.addIncomingAssociation(entityType, incomingOneRelation);
						}
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

		return this.equals(type) || isSubType(type);
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
		if (superEntityType != null)
		{
			return superEntityType.isEqual(entity, other);
		}
		else
		{

			return true;
		}
	}

	private boolean isSubType(Type<?> type)
	{
		for (EntityType<?> subType : getSubEntityTypes(true))
		{
			if (subType.equals(type))
			{
				return true;
			}
			else
			{
				boolean assignableToSubType = subType.isAssignableFrom(subType);
				if (assignableToSubType)
				{
					return true;
				}
			}
		}
		return false;
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
		visitAttributes(this, visitor, context);
		EntityType<?> currentType = this;
		while (currentType.getSuperEntityType() != null)
		{
			if (visitor.visitSuperView(context, currentType.getSuperEntityType()))
			{
				visitAttributes(currentType.getSuperEntityType(), visitor, context);
				visitSubTypes(currentType.getSuperEntityType(), currentType, visitor, context);
			}
			else
			{
				break;
			}
			currentType = currentType.getSuperEntityType();
		}
		visitSubTypes(this, null, visitor, context);
	}

	protected <C> void visitAttributes(EntityType<?> entityType, ViewVisitor<C> visitor, C context)
	{
		for (Attribute<?, ?> attribute : entityType.getDeclaredAttributes())
		{
			if (attribute.getTargetType() instanceof PrimitiveType)
			{
				visitor.visit(context, attribute);
			}
			else
			{
				Type<?> targetType = attribute.getTargetType();
				if (targetType != null)
				{
					visitor.visit(context, attribute, new AttributeVisitor<C>(visitor, (View) targetType));
				}
				else
				{
					visitor.visit(context, attribute);
				}
			}
		}
	}

	protected <C> void visitSubTypes(EntityType<?> entityType, EntityType<?> originatingSubEntityType,
		ViewVisitor<C> visitor, C context)
	{
		for (EntityType<?> subType : entityType.getSubEntityTypes())
		{
			if (subType != originatingSubEntityType && visitor.visitSubView(context, subType))
			{
				visitAttributes(subType, visitor, context);
				visitSubTypes(subType, null, visitor, context);
			}
		}
	}

}
