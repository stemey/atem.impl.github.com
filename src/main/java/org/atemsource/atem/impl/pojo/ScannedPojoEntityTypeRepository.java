/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.AbstractMetaDataRepository;
import org.atemsource.atem.impl.common.infrastructure.CandidateByAnnotationResolver;
import org.atemsource.atem.impl.common.infrastructure.CandidateResolver;
import org.atemsource.atem.impl.common.infrastructure.ClasspathScanner;
import org.atemsource.atem.impl.infrastructure.BeanCreator;
import org.atemsource.atem.impl.pojo.attribute.AttributeFactory;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.atemsource.atem.spi.EntityTypeSubrepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ScannedPojoEntityTypeRepository extends
		AbstractMetaDataRepository<Object> implements
		EntityTypeSubrepository<Object> {
	private List<AttributeFactory> attributeFactories = new ArrayList<AttributeFactory>();

	@Autowired
	private BeanCreator beanCreator;

	private CandidateResolver candidateResolver;

	private Class entityClass;

	private Class<? extends AbstractEntityType> entityTypeClass;

	private String includedPackage;

	protected Set<String> nonAvailableEntityNames = new HashSet<String>();

	@Inject
	private ClasspathScanner scanner;

	protected Attribute addAttribute(
			AbstractEntityType entityType,
			org.atemsource.atem.impl.pojo.attribute.PropertyDescriptor propertyDescriptor) {
		Attribute attribute = null;
		for (AttributeFactory attributeFactory : attributeFactories) {
			if (attributeFactory.canCreate(propertyDescriptor,
					entityTypeCreationContext)) {
				attribute = attributeFactory.createAttribute(entityType,
						propertyDescriptor, entityTypeCreationContext);
				break;
			}
		}
		return attribute;
	}

	private PropertyDescriptorFactory propertyDescriptorFactory;

	protected void addAttributes(AbstractEntityType entityType) {
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (org.atemsource.atem.impl.pojo.attribute.PropertyDescriptor propertyDescriptor : propertyDescriptorFactory
				.getPropertyDescriptors(entityType.getEntityClass())) {

			Attribute attribute = addAttribute(entityType, propertyDescriptor);
			if (attribute != null) {
				attributes.add(attribute);
			}

		}

		entityType.setAttributes(attributes);
	}

	public PropertyDescriptorFactory getPropertyDescriptorFactory() {
		return propertyDescriptorFactory;
	}

	public void setPropertyDescriptorFactory(
			PropertyDescriptorFactory propertyDescriptorFactory) {
		this.propertyDescriptorFactory = propertyDescriptorFactory;
	}

	@Override
	public void afterFirstInitialization(
			EntityTypeRepository entityTypeRepositoryImpl) {
	}

	@Override
	public void afterInitialization() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(EntityType entityType) {
		if (entityTypeClass.isInstance(entityType)) {
			return true;
		} else {
			return false;
		}
	}

	protected synchronized AbstractEntityType createEntityType(final Class clazz) {
		AbstractEntityType entityType;
		entityType = beanCreator.create(entityTypeClass);
		entityType.setEntityClass(clazz);
		entityType.setCode(clazz.getName());
		nameToEntityTypes.put(clazz.getName(), entityType);
		classToEntityTypes.put(clazz, entityType);
		entityTypes.add(entityType);
		return entityType;
	}

	protected EntityType createEntityType(String entityName) {
		Class clazz;
		try {
			clazz = Class.forName(entityName);
		} catch (ClassNotFoundException e) {
			return null;
		}
		if (!isAvailable(clazz)) {
			return null;
		}
		return createEntityType(clazz);
	}

	public List<AttributeFactory> getAttributeFactories() {
		return attributeFactories;
	}

	public Class getEntityClass() {
		return entityClass;
	}

	@Override
	public EntityType getEntityType(Class clazz) {
		if (isAvailable(clazz)) {
			return null;
		}
		EntityType entityType = super.getEntityType(clazz);
		return entityType;
	}

	@Override
	public EntityType<Object> getEntityType(Object entity) {
		if (entity == null) {
			return null;
		} else {
			return getEntityType(entity.getClass());
		}
	}

	@Override
	public EntityType getEntityType(String entityName) {
		return super.getEntityType(entityName);
	}

	@Override
	public EntityType getEntityTypeReference(Class clazz) {
		if (isAvailable(clazz)) {
			return null;
		}
		return this.classToEntityTypes.get(clazz);
	}

	@Override
	public EntityType getEntityTypeReference(String entityName) {
		Class clazz;
		try {
			clazz = Class.forName(entityName);
		} catch (ClassNotFoundException e) {
			return null;
		}
		return getEntityTypeReference(clazz);
	}

	public String getIncludedPackage() {
		return includedPackage;
	}

	@Override
	public void initialize(EntityTypeCreationContext entityTypeCreationContext) {
		this.entityTypeCreationContext = entityTypeCreationContext;
		scan();
	}

	protected void initializeEntityType(AbstractEntityType entityType) {
		Class clazz = entityType.getEntityClass();
		final Class superclass = clazz.getSuperclass();
		if (superclass != null && !superclass.equals(Object.class)) {
			final EntityType superType = getEntityTypeReference(superclass);
			entityType.setSuperEntityType(superType);
			// TODO this only works for AbstractEntityType but actually should
			// work for all.
			if (superType instanceof AbstractEntityType) {
				((AbstractEntityType) superType).addSubEntityType(entityType);
			}
		}
		addAttributes(entityType);

		attacheServicesToEntityType(entityType);
		entityType.initializeIncomingAssociations(entityTypeCreationContext);

	}

	protected boolean isAvailable(Class clazz) {
		return (entityClass != null && !entityClass.isAssignableFrom(clazz))
				|| (includedPackage != null && !clazz.getPackage().getName()
						.startsWith(includedPackage));
	}

	private boolean isAvailable(String entityName) {
		AbstractEntityType<Object> abstractEntityType = nameToEntityTypes
				.get(entityName);
		if (abstractEntityType != null) {
			return true;
		}
		try {
			Class.forName(entityName);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	private void scan() {
		try {

			Collection<Class<?>> classes = scanner.findClasses(includedPackage,
					candidateResolver);

			for (Class<?> clazz : classes) {
				String entityName = clazz.getName();
				if (entityTypeCreationContext.hasEntityTypeReference(clazz)) {
					continue;
				}
				AbstractEntityType entityType = createEntityType(clazz);
			}

			for (AbstractEntityType<?> entityType : nameToEntityTypes.values()) {
				initializeEntityType(entityType);
			}
		} catch (IOException e) {
			throw new TechnicalException("cannot scan package "
					+ includedPackage, e);
		}
	}

	public void setAnnotationTypes(
			Set<Class<? extends Annotation>> annotationTypes) {
		annotationTypes = new HashSet<Class<? extends Annotation>>();
		annotationTypes.add(org.atemsource.atem.api.type.Entity.class);
		this.candidateResolver = new CandidateByAnnotationResolver(
				annotationTypes);
	}

	public void setAttributeFactories(List<AttributeFactory> attributeFactories) {
		this.attributeFactories = attributeFactories;
	}

	public void setCandidateResolver(CandidateResolver candidateResolver) {
		this.candidateResolver = candidateResolver;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public void setEntityTypeClass(
			Class<? extends AbstractEntityType> entityTypeClass) {
		this.entityTypeClass = entityTypeClass;
	}

	public void setIncludedPackage(String includedPackage) {
		this.includedPackage = includedPackage;
	}

}
