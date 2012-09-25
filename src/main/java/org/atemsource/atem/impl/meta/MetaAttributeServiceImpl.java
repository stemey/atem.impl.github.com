/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.meta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.AssociationAttribute;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.extension.EntityTypeRepositoryPostProcessor;
import org.atemsource.atem.api.extension.MetaAttributeService;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.service.PersistenceService;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.EntityTypeBuilder;
import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.impl.common.attribute.AbstractSingleAssociationAttribute;
import org.atemsource.atem.spi.DynamicEntityTypeSubrepository;
import org.atemsource.atem.spi.EntityTypeCreationContext;

public class MetaAttributeServiceImpl implements MetaDataService,
		MetaAttributeService, EntityTypeRepositoryPostProcessor {

	private Map<String, MetaDataStore<?>> data = new HashMap<String, MetaDataStore<?>>();
	private EntityTypeCreationContext entityTypeCreationContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.atemsource.atem.impl.meta.MS#addSingleMetaAttribute(java.lang.String,
	 * org.atemsource.atem.api.type.EntityType,
	 * org.atemsource.atem.api.type.EntityType)
	 */
	@Override
	public <J> SingleAttribute<J> addSingleMetaAttribute(String name,
			EntityType<?> holderType, EntityType<J> metaDataType) {

		MetaAttribute attribute = new MetaAttribute(holderType, metaDataType,
				this, name);

		MetaDataStore store = data.get(attribute.getCode());
		if (store == null) {
			store = new MetaDataStore(attribute);
			data.put(attribute.getCode(), store);
		}
		entityTypeCreationContext.addMetaAttribute(holderType, attribute);

		return attribute;
	}



	@Override
	public void initialize(EntityTypeCreationContext ctx) {
		this.entityTypeCreationContext = ctx;
	}

	@Override
	public Object getMetaData(Object entity, MetaAttribute attribute) {
		MetaDataStore store = data.get(attribute.getCode());
		if (store == null) {
			store = new MetaDataStore(attribute);
			data.put(attribute.getCode(), store);
			return null;
		} else {
			return store.find(entity);
		}
	}

	@Override
	public void setMetaData(Object entity, Object value, MetaAttribute attribute) {
		MetaDataStore store = data.get(attribute.getCode());

		store.insert(entity, value);
	}





}
