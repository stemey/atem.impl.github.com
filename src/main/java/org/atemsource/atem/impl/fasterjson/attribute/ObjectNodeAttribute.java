/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.fasterjson.attribute;

import org.atemsource.atem.impl.common.attribute.AbstractSingleAssociationAttribute;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class ObjectNodeAttribute extends AbstractSingleAssociationAttribute<ObjectNode>
{
	@Override
	public ObjectNode getValue(Object entity)
	{
		ObjectNode node = (ObjectNode) entity;
		if (node.isNull())
		{
			throw new NullPointerException("entity is null");
		}
		else
		{
			JsonNode jsonNode = node.get(getCode());
			if (jsonNode == null || jsonNode.isNull())
			{
				return null;
			}
			else
			{
				return (ObjectNode) jsonNode;
			}
		}
	}

	@Override
	public boolean isWriteable()
	{
		return true;
	}

	@Override
	public void setValue(Object entity, ObjectNode value)
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
			node.put(getCode(), value);
		}
	}

}
