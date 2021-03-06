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
import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.atemsource.atem.api.infrastructure.util.ReflectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class EntityTypeEntityType extends AbstractEntityType<Type<?>>
{

	public EntityTypeEntityType()
	{
	}

	@Override
	public Class<Type<?>> getJavaType()
	{
		return getEntityClass();
	}

	@Override
	public boolean isInstance(Object entity)
	{
		return getEntityClass().isInstance(entity);
	}

	@Override
	public boolean isAssignableFrom(Type<?> type) {
		// currently interfaces are not part of the entityType hierachy.
		if (type instanceof EntityType<?>) {
			return getEntityClass().isAssignableFrom(((EntityType<?>)type).getEntityClass());
		}else{
			return false;
		}
	}

}
