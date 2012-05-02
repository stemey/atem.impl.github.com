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


import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.impl.infrastructure.ReflectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class EntityTypeEntityType extends AbstractEntityType<EntityType<?>>
{

	public EntityTypeEntityType()
	{
	}

	@Override
	public Class<EntityType<?>> getJavaType()
	{
		return ReflectionUtils.getActualTypeParameter(getClass(), EntityType.class);
	}

	@Override
	public boolean isAssignableFrom(Object entity)
	{
		return entity instanceof EntityType;
	}

}
