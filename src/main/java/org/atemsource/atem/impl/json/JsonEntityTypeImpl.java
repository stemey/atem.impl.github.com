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


import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.TechnicalException;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.dynamic.DynamicEntity;
import org.atemsource.atem.impl.infrastructure.BeanLocator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class JsonEntityTypeImpl extends AbstractEntityType<ObjectNode>
{

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private BeanLocator beanLocator;

	private String typeProperty;

	public JsonEntityTypeImpl()
	{
		super();
		setEntityClass(ObjectNode.class);
	}

	@Override
	public ObjectNode createEntity() throws TechnicalException
	{
		ObjectNode objectNode = objectMapper.createObjectNode();
		if (typeProperty != null)
		{
			objectNode.put(typeProperty, getCode());
		}
		return objectNode;
	}

	public Class<ObjectNode> getJavaType()
	{
		return ObjectNode.class;
	}

	public String getTypeProperty()
	{
		return typeProperty;
	}

	public boolean isAssignableFrom(Object entity)
	{
		if (entity == null)
		{
			return false;
		}
		else if (entity instanceof DynamicEntity)
		{
			String typeCode = ((DynamicEntity) entity).getTypeCode();
			EntityType<Object> entityType = entityTypeRepository.getEntityType(typeCode);
			return isAssignableFrom(entityType);
		}
		else
		{
			return false;
		}
	}

	public boolean isPersistable()
	{
		return true;
	}

	public void setTypeProperty(String typeProperty)
	{
		this.typeProperty = typeProperty;
	}
}
