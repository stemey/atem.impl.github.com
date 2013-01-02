/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.dynamic;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.DynamicEntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class DynamicEntityTypeImpl extends DynamicEntityType<DynamicEntity>
{

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	private boolean serializingPrimitives;

	public DynamicEntityTypeImpl()
	{
		super();
		setEntityClass(DynamicEntityImpl.class);
	}

	@Override
	public DynamicEntity createEntity() throws TechnicalException
	{
		DynamicEntityImpl dynamicEntityImpl = new DynamicEntityImpl();
		dynamicEntityImpl.setTypeCode(getCode());
		return dynamicEntityImpl;
	}

	@Override
	public Class<DynamicEntity> getJavaType()
	{
		return DynamicEntity.class;
	}

	@Override
	public boolean isInstance(Object entity)
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

	public boolean isSerializingPrimitives()
	{
		return serializingPrimitives;
	}

	public void setSerializingPrimitives(boolean serializingPrimitives)
	{
		this.serializingPrimitives = serializingPrimitives;
	}

}
