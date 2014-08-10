/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.dynamic;

import org.atemsource.atem.api.BeanLocator;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.EntityTypeBuilder;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.DynamicEntityType;
import org.atemsource.atem.impl.common.AbstractEntityTypeBuilder.EntityTypeBuilderCallback;
import org.atemsource.atem.impl.common.AbstractMetaDataRepository;
import org.atemsource.atem.spi.DynamicEntityTypeSubrepository;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;

public class DynamicEntityTypeRepository extends
		AbstractMetaDataRepository<DynamicEntity> implements
		DynamicEntityTypeSubrepository<DynamicEntity>,
		EntityTypeBuilderCallback {
	@Autowired
	private BeanLocator beanLocator;

	private boolean serializingPrimitiveValues;

	@Override
	public void afterFirstInitialization(
			EntityTypeRepository entityTypeRepositoryImpl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterInitialization() {
		// TODO Auto-generated method stub

	}

	@Override
	public EntityTypeBuilder createBuilder(String code) {
		DynamicEntityTypeBuilder entityTypeBuilder = beanLocator
				.getInstance(DynamicEntityTypeBuilder.class);
		entityTypeBuilder.setEntityType(createEntityType(code));
		entityTypeBuilder.setEntityClass(DynamicEntityImpl.class);
		entityTypeBuilder.setSerializingPrimitives(serializingPrimitiveValues);
		entityTypeBuilder.setRepositoryCallback(this);
		return entityTypeBuilder;
	}

	
	public DynamicEntityTypeImpl createEntityType(String code) {
		final DynamicEntityTypeImpl dynamicEntityTypeImpl = beanLocator
				.getInstance(DynamicEntityTypeImpl.class);
		dynamicEntityTypeImpl.setCode(code);
		dynamicEntityTypeImpl.setRepository(this);
		if (getEntityType(code) != null) {
			throw new IllegalArgumentException("dynamic type with name " + code
					+ " already exists.");
		}
		this.nameToEntityTypes.put(code, dynamicEntityTypeImpl);
		this.entityTypes.add(dynamicEntityTypeImpl);
		dynamicEntityTypeImpl
				.setSerializingPrimitives(serializingPrimitiveValues);
		attacheServicesToEntityType(dynamicEntityTypeImpl);
		return dynamicEntityTypeImpl;
	}

	@Override
	public EntityType<DynamicEntity> getEntityType(Object entity) {
		try {
			DynamicEntity dynamicEntity = (DynamicEntity) entity;
			return getEntityType(dynamicEntity.getTypeCode());
		} catch (ClassCastException e) {
			return null;
		}
	}

	@Override
	public void initialize(EntityTypeCreationContext entityTypeCreationContext) {
		this.entityTypeCreationContext = entityTypeCreationContext;
	}

	public boolean isSerializingPrimitiveValues() {
		return serializingPrimitiveValues;
	}

	@Override
	public void onFinished(AbstractEntityType<?> entityType) {
		entityType.setMetaType((EntityType) entityTypeCreationContext
				.getEntityTypeReference(EntityType.class));
		attacheServicesToEntityType(entityType);
		((AbstractEntityType) entityType)
				.initializeIncomingAssociations(entityTypeCreationContext);
		entityTypeCreationContext.lazilyInitialized(entityType);
	}

	public void setSerializingPrimitiveValues(boolean serializingPrimitiveValues) {
		this.serializingPrimitiveValues = serializingPrimitiveValues;
	}

	@Override
	public EntityTypeBuilder replaceBuilder(String code) {
		throw new UnsupportedOperationException("not implemented yet");
	}

}
