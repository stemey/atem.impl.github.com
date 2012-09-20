/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.meta;

import java.io.Serializable;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.type.EntityType;


public class MetaIdentityService implements IdentityService
{

	@Override
	public Serializable getId(EntityType<?> entityType, Object entity)
	{
		if (entity == null)
		{
			throw new NullPointerException("cannot get id of null");
		}
		else if (entity instanceof Attribute<?, ?>)
		{
			Attribute<?, ?> attribute = (Attribute<?, ?>) entity;
			return attribute.getEntityType().getCode() + ":" + attribute.getCode();
		}
		else if (entity instanceof EntityType<?>)
		{
			EntityType<?> objectEntityType = (EntityType<?>) entity;
			return objectEntityType.getCode();
		}
		else
		{
			throw new IllegalStateException("can only handle attribute and entity type");
		}
	}
}
