/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.json.attribute;

import java.util.Iterator;

import org.atemsource.atem.api.type.Type;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class ChildrenAttribute extends AbstractMapNodeAttribute<JsonNode>
{

	@Override
	public Type<JsonNode> getTargetType(JsonNode value)
	{
		if (value instanceof ObjectNode)
		{
			return getEntityType();
		}
		else
		{
			return entityTypeRepository.getType(value);
		}
	}

	@Override
	public ObjectNode getValue(Object entity)
	{
		return (ObjectNode) entity;
	}

	@Override
	public void setValue(Object entity, ObjectNode value)
	{
		ObjectNode oldNode = (ObjectNode) entity;
		oldNode.removeAll();
		Iterator<String> fieldNames = value.getFieldNames();
		while (fieldNames.hasNext())
		{
			String fieldName = fieldNames.next();
			oldNode.put(fieldName, value.get(fieldName));
		}
	}

}
