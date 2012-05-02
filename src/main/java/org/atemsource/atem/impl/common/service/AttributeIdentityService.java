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
package org.atemsource.atem.impl.common.service;


import java.io.Serializable;

import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.type.EntityType;


public class AttributeIdentityService implements IdentityService
{
	private String attributeCode;

	public String getAttributeCode()
	{
		return attributeCode;
	}

	@Override
	public Serializable getId(EntityType<?> entityType, Object entity)
	{
		return (Serializable) entityType.getAttribute(attributeCode).getValue(entity);
	}

	public void setAttributeCode(String attributeCode)
	{
		this.attributeCode = attributeCode;
	}

}
