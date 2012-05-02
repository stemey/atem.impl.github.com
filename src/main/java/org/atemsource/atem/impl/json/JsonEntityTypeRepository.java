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
package org.atemsource.atem.impl.json;


import org.atemsource.atem.api.DynamicEntityTypeSubrepository;
import org.atemsource.atem.api.EntityTypeBuilder;
import org.atemsource.atem.api.EntityTypeCreationContext;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.AbstractMetaDataRepository;
import org.atemsource.atem.impl.common.AbstractEntityTypeBuilder.EntityTypeBuilderCallback;
import org.atemsource.atem.impl.infrastructure.BeanLocator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;


public class JsonEntityTypeRepository extends AbstractMetaDataRepository<ObjectNode> implements
	DynamicEntityTypeSubrepository<ObjectNode>, EntityTypeBuilderCallback
{

	@Autowired
	private BeanLocator beanLocator;

	private String typeProperty = "_type";

	public void afterFirstInitialization(EntityTypeRepository entityTypeRepositoryImpl)
	{
		// TODO Auto-generated method stub

	}

	public void afterInitialization()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public EntityTypeBuilder createBuilder(String code)
	{
		JsonEntityTypeBuilder builder = beanLocator.getInstance(JsonEntityTypeBuilder.class);
		JsonEntityTypeImpl entityType = createEntityType(code);
		builder.setEntityType(entityType);
		builder.setEntityClass(ObjectNode.class);
		builder.setRepositoryCallback(this);
		return builder;
	}

	public JsonEntityTypeImpl createEntityType(String code)
	{
		final JsonEntityTypeImpl dynamicEntityTypeImpl = beanLocator.getInstance(JsonEntityTypeImpl.class);
		dynamicEntityTypeImpl.setCode(code);
		dynamicEntityTypeImpl.setTypeProperty(typeProperty);

		if (getEntityType(code) != null)
		{
			throw new IllegalArgumentException("dynamic type with name " + code + " already exists.");
		}
		AbstractEntityType x = dynamicEntityTypeImpl;
		this.nameToEntityTypes.put(code, x);
		return dynamicEntityTypeImpl;
	}

	@Override
	public EntityType<ObjectNode> getEntityType(Object entity)
	{
		try
		{
			ObjectNode objectNode = (ObjectNode) entity;
			JsonNode jsonNode = objectNode.get(typeProperty);
			if (jsonNode != null)
			{
				return getEntityType(jsonNode.getTextValue());
			}
			else
			{
				return null;
			}
		}
		catch (ClassCastException e)
		{
			return null;
		}
	}

	public String getTypeProperty()
	{
		return typeProperty;
	}

	public void initialize(EntityTypeCreationContext entityTypeCreationContext)
	{

	}

	@Override
	public void onFinished(AbstractEntityType<?> entityType)
	{
		attacheServicesToEntityType((AbstractEntityType) entityType);
		((AbstractEntityType) entityType).initializeIncomingAssociations(entityTypeCreationContext);
		entityTypeCreationContext.lazilyInitialized(entityType);
	}

	public void setTypeProperty(String typeProperty)
	{
		this.typeProperty = typeProperty;
	}
}
