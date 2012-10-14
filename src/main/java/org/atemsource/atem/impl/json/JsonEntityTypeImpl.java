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
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.DynamicEntityType;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class JsonEntityTypeImpl extends DynamicEntityType<ObjectNode> {

	@Autowired
	private BeanLocator beanLocator;

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	private ObjectMapper objectMapper;

	private TypeCodeConverter typeCodeConverter;

	private String typeProperty;

	public JsonEntityTypeImpl() {
		super();
		setEntityClass(ObjectNode.class);
	}

	@Override
	public ObjectNode createEntity() throws TechnicalException {
		ObjectNode objectNode = objectMapper.createObjectNode();
		if (typeProperty != null) {
			if (typeCodeConverter != null) {
				objectNode.put(typeProperty,
						typeCodeConverter.toExternalCode(this));
			} else {
				objectNode.put(typeProperty, this.getCode());
			}
		}
		return objectNode;
	}

	public String getExternalTypeCode() {
		if (typeCodeConverter == null) {
			return getCode();
		} else {
			return typeCodeConverter.toExternalCode(this);
		}
	}

	@Override
	public Class<ObjectNode> getJavaType() {
		return ObjectNode.class;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public String getTypeProperty() {
		return typeProperty;
	}

	@Override
	public boolean isInstance(Object entity) {
		if (entity == null) {
			return false;
		} else if (entity instanceof ObjectNode) {
			JsonNode typePropertyNode = ((ObjectNode) entity).get(typeProperty);
			if (typePropertyNode == null) {
				return false;
			}
			String typeCode = typePropertyNode.getTextValue();
			if (typeCodeConverter != null) {
				String versionedTypeCode = typeCodeConverter.fromExternalCode(
						this, typeCode);
				EntityType<Object> entityType = entityTypeRepository
						.getEntityType(versionedTypeCode);
				return isAssignableFrom(entityType);
			} else {
				EntityType<Object> entityType = entityTypeRepository
						.getEntityType(typeCode);
				return isAssignableFrom(entityType);

			}
		} else {
			return false;
		}
	}

	public boolean isPersistable() {
		return true;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void setTypeCodeConverter(TypeCodeConverter typeCodeConverter) {
		this.typeCodeConverter = typeCodeConverter;
	}

	public void setTypeProperty(String typeProperty) {
		this.typeProperty = typeProperty;
	}
}
