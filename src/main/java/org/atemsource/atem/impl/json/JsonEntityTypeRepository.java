/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.json;

import org.atemsource.atem.api.BeanLocator;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.EntityTypeBuilder;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.common.AbstractEntityTypeBuilder.EntityTypeBuilderCallback;
import org.atemsource.atem.impl.common.AbstractMetaDataRepository;
import org.atemsource.atem.impl.json.attribute.ChildrenAttribute;
import org.atemsource.atem.impl.json.attribute.PropertiesAttribute;
import org.atemsource.atem.spi.DynamicEntityTypeSubrepository;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;


public class JsonEntityTypeRepository extends AbstractMetaDataRepository<ObjectNode> implements
	DynamicEntityTypeSubrepository<ObjectNode>, EntityTypeBuilderCallback
{
	private JsonEntityTypeImpl arrayNodeType;

	@Autowired
	private BeanLocator beanLocator;

	private ObjectMapper objectMapper;

	private JsonEntityTypeImpl objectNodeType;

	@Override
	public void clear() {
		
		super.clear();
		initGenericTypes();
	}

	private TypeCodeConverter typeCodeConverter;

	private String typeProperty = "_type";

	@Override
	public void afterFirstInitialization(EntityTypeRepository entityTypeRepositoryImpl)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void afterInitialization()
	{
	}

	@Override
	public EntityTypeBuilder createBuilder(String code)
	{
		JsonEntityTypeBuilder builder = beanLocator.getInstance(JsonEntityTypeBuilder.class);
		JsonEntityTypeImpl entityType = createEntityType(code);
		builder.setEntityType(entityType);
		builder.setEntityClass(ObjectNode.class);
		builder.setRepositoryCallback(this);
		builder.setObjectMapper(objectMapper);
		return builder;
	}

	public JsonEntityTypeImpl createEntityType(String code)
	{
		final JsonEntityTypeImpl dynamicEntityTypeImpl = beanLocator.getInstance(JsonEntityTypeImpl.class);
		dynamicEntityTypeImpl.setTypeCodeConverter(typeCodeConverter);
		dynamicEntityTypeImpl.setRepository(this);
		dynamicEntityTypeImpl.setCode(code);
		dynamicEntityTypeImpl.setObjectMapper(objectMapper);
		dynamicEntityTypeImpl.setTypeProperty(typeProperty);

		if (getEntityType(code) != null)
		{
			throw new IllegalArgumentException("dynamic type with name " + code + " already exists.");
		}
		this.nameToEntityTypes.put(code, dynamicEntityTypeImpl);
		entityTypes.add(dynamicEntityTypeImpl);
		return dynamicEntityTypeImpl;
	}

	@Override
	public EntityType<ObjectNode> getEntityType(Object entity)
	{
		if (entity instanceof ArrayNode)
		{
			return arrayNodeType;
		}
		else if (entity instanceof ObjectNode)
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
					return objectNodeType;
				}
			}
			catch (ClassCastException e)
			{
				return null;
			}
		}
		else if (entity instanceof JsonNode)
		{
			return null;
		}

		return null;
	}

	public ObjectMapper getObjectMapper()
	{
		return objectMapper;
	}

	public String getTypeProperty()
	{
		return typeProperty;
	}

	public void initGenericTypes()
	{
		objectNodeType = createEntityType(ObjectNode.class.getName());

		classToEntityTypes.put(ObjectNode.class, objectNodeType);
		// TODO type error
		// classToEntityTypes.put(ArrayNode.class, arrayNodeType);
	}

	@Override
	public void initialize(EntityTypeCreationContext entityTypeCreationContext)
	{
		initGenericTypes();
		this.entityTypeCreationContext = entityTypeCreationContext;
		PropertiesAttribute mapAttribute = beanLocator.getInstance(PropertiesAttribute.class);
		mapAttribute.setCode("properties");
		mapAttribute.setEntityType(objectNodeType);
		objectNodeType.addAttribute(mapAttribute);
		onFinished(objectNodeType);

		arrayNodeType = createEntityType(ArrayNode.class.getName());
		ChildrenAttribute childrenAttribute = beanLocator.getInstance(ChildrenAttribute.class);
		childrenAttribute.setCode("children");
		childrenAttribute.setEntityType(arrayNodeType);
		arrayNodeType.addAttribute(childrenAttribute);
		onFinished(arrayNodeType);
	}

	@Override
	public void onFinished(AbstractEntityType<?> entityType)
	{
		entityType.setMetaType( (EntityType) entityTypeCreationContext.getEntityTypeReference(EntityType.class));
		attacheServicesToEntityType(entityType);
		((AbstractEntityType) entityType).initializeIncomingAssociations(entityTypeCreationContext);
		entityTypeCreationContext.lazilyInitialized(entityType);
	}

	public void setObjectMapper(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	public void setTypeCodeConverter(TypeCodeConverter typeCodeConverter)
	{
		this.typeCodeConverter = typeCodeConverter;
	}

	public void setTypeProperty(String typeProperty)
	{
		this.typeProperty = typeProperty;
	}

	@Override
	public EntityTypeBuilder replaceBuilder(String code) {
		EntityType<?> oldType=getEntityType(code);
		nameToEntityTypes.remove(code);
		entityTypes.remove(oldType);
		return createBuilder(code);
	}
}
