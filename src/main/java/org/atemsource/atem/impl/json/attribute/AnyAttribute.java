/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.json.attribute;

import org.atemsource.atem.impl.json.JsonUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class AnyAttribute extends JsonAttribute<Object>
{
	@Override
	public Object getValue(Object entity)
	{
		ObjectNode node = (ObjectNode) entity;
		if (node.isNull())
		{
			throw new NullPointerException("entity is null");
		}
		else
		{
			JsonNode jsonNode = node.get(getCode());
			if (jsonNode.isNull())
			{
				return null;
			}
			else
			{
				return JsonUtils.convertToJava(jsonNode);
			}
		}
	}

	@Override
	public boolean isWriteable()
	{
		return true;
	}

	@Override
	public void setValue(Object entity, Object value)
	{
		ObjectNode node = (ObjectNode) entity;
		if (node.isNull())
		{
			throw new NullPointerException("entity is null");
		}
		if (value == null)
		{
			node.putNull(getCode());
		}
		else
		{
			JsonNode jsonNode = JsonUtils.convertToJson(value);
			node.put(getCode(), jsonNode);
		}
	}

}
