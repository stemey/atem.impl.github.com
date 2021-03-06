/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.common.service;

import java.io.Serializable;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.primitive.SimpleTextType;


public class AttributeIdentityService implements IdentityService
{
	private String attributeCode;

	private final Type<?> idType = new SimpleTextType();

	public String getAttributeCode()
	{
		return attributeCode;
	}

	@Override
	public <E> Serializable getId(EntityType<E> entityType, E entity)
	{
		return (Serializable) entityType.getAttribute(attributeCode).getValue(entity);
	}

	@Override
	public Type<?> getIdType(EntityType<?> entityType)
	{
		return idType;
	}

	public void setAttributeCode(String attributeCode)
	{
		this.attributeCode = attributeCode;
	}

}
