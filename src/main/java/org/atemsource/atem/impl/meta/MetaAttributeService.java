/*******************************************************************************
 * Stefan Meyer, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.meta;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.atemsource.atem.api.DynamicEntityTypeSubrepository;
import org.atemsource.atem.api.EntityTypeBuilder;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.AssociationAttribute;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.attribute.primitive.PrimitiveType;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.service.PersistenceService;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.attribute.SingleAssociationAttribute;


public class MetaAttributeService
{

	private static final String ID_ATTRIBUTE = "id";

	private static final String HOLDER_ATTRIBUTE = "holder";

	private static final String META_DATA_ATTRIBUTE = "metaData";

	private DynamicEntityTypeSubrepository<?> dynamicEntityTypeSubrepository;

	private Map<String, Attribute<?, ?>> metaAttributes = new HashMap<String, Attribute<?, ?>>();

	@Inject
	private EntityTypeRepository entityTypeRepository;

	public <J> SingleMetaAttribute<J> addSingleMetaAttribute(String name, EntityType<?> holderType,
		EntityType<J> metaDataType)
	{
		EntityTypeBuilder builder = dynamicEntityTypeSubrepository.createBuilder(name);
		SingleAssociationAttribute<?> metaDataAttribute =
			(SingleAssociationAttribute<?>) builder.addSingleAssociationAttribute(META_DATA_ATTRIBUTE, metaDataType);
		metaDataAttribute.setTargetCardinality(Cardinality.ONE);
		SingleAssociationAttribute<?> holderAttribute =
			(SingleAssociationAttribute<?>) builder.addSingleAssociationAttribute(HOLDER_ATTRIBUTE, holderType);
		holderAttribute.setTargetCardinality(Cardinality.ONE);
		builder.addPrimitiveAttribute(ID_ATTRIBUTE, (PrimitiveType<J>) entityTypeRepository.getType(String.class));
		EntityType<?> entityType = builder.createEntityType();

		PersistenceService persistenceService = entityType.getService(PersistenceService.class);
		if (persistenceService == null)
		{
			throw new IllegalArgumentException("cannot add metaData via an unpersistable intermediate type");
		}
		IdentityService holderIdentityService = holderType.getService(IdentityService.class);
		if (holderIdentityService == null)
		{
			throw new IllegalArgumentException("holder type " + holderType.getCode() + " must provide IdentityService");
		}
		SingleMetaAttribute singleMetaAttribute = new SingleMetaAttribute(metaDataAttribute, holderAttribute, this);
		metaAttributes.put(name, singleMetaAttribute);
		return singleMetaAttribute;
	}

	public DynamicEntityTypeSubrepository<?> getDynamicEntityTypeSubrepository()
	{
		return dynamicEntityTypeSubrepository;
	}

	public <J> SingleMetaAttribute<J> getMetaAttribute(String name)
	{
		return (SingleMetaAttribute<J>) metaAttributes.get(name);
	}

	public Object getMetaData(String name, Object holder)
	{
		EntityType<Object> entityType = entityTypeRepository.getEntityType(name);
		AssociationAttribute holderAttribute = (AssociationAttribute) entityType.getAttribute(HOLDER_ATTRIBUTE);
		Attribute<?, ?> incomingRelation = holderAttribute.getIncomingRelation();
		Object intermediate = incomingRelation.getValue(holder);
		if (intermediate == null)
		{
			intermediate = entityType.createEntity();
		}
		holderAttribute.setValue(intermediate, holder);
		PersistenceService persistenceService = entityType.getService(PersistenceService.class);
		if (!persistenceService.isPersistent(intermediate))
		{
			Serializable id =
				((IdentityService) holderAttribute.getTargetType().getService(IdentityService.class)).getId(
					holderAttribute.getEntityType(), holder);
			entityType.getAttribute(ID_ATTRIBUTE).setValue(intermediate, id.toString());
			persistenceService.insert(intermediate);
		}
		return intermediate;
	}

	public void setDynamicEntityTypeSubrepository(DynamicEntityTypeSubrepository<?> dynamicEntityTypeSubrepository)
	{
		this.dynamicEntityTypeSubrepository = dynamicEntityTypeSubrepository;
	}

}
