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


import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.atemsource.atem.api.attribute.Accessor;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.TypedId;
import org.atemsource.atem.impl.pojo.SerializableTypeId;


public class LocalAccessor implements Accessor
{

	private Map<TypedId, Object> values = new HashMap<TypedId, Object>();

	private EntityType<?> entityType;

	public LocalAccessor(EntityType<?> entityType)
	{
		this.entityType = entityType;
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass)
	{
		return null;
	}

	@Override
	public Annotation getAnnotationAnnotatedBy(Class<? extends Annotation> annotationClass)
	{
		return null;
	}

	@Override
	public Collection<? extends Annotation> getAnnotations()
	{
		return null;
	}

	private TypedId getKey(Object entity)
	{
		Serializable id = entityType.getService(IdentityService.class).getId(entityType, entity);
		return new SerializableTypeId(id, entityType.getJavaType());
	}

	@Override
	public Object getValue(Object entity)
	{
		Object value = values.get(getKey(entity));
		if (value == null)
		{
			value = new HashSet();
			values.put(getKey(entity), value);
		}
		return value;
	}

	@Override
	public boolean isReadable()
	{
		return true;
	}

	@Override
	public boolean isWritable()
	{
		return true;
	}

	@Override
	public void setValue(Object entity, Object value)
	{
		values.put(getKey(entity), value);
	}

}
