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
package org.atemsource.atem.impl.common.attribute;


import java.io.Serializable;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.EqualityUtils;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;


@Ignore
public class AttributeTest
{

	@Autowired
	protected EntityTypeRepository entityTypeRepository;

	public AttributeTest()
	{
		super();
	}

	protected void assertEquals(final Object o1, final Object o2)
	{
		Assert.assertTrue(EqualityUtils.isEqual((Serializable) o1, (Serializable) o2));
	}

	protected EntityB createEntity(String text, int number)
	{
		final EntityB2 entityB = new EntityB2();
		entityB.setInteger(number);
		EntityA singleA = new EntityA();
		singleA.setString(text);
		entityB.setSingleA(singleA);
		return entityB;
	}

	protected Attribute getAttribute(String property)
	{
		EntityType entityType = entityTypeRepository.getEntityType(EntityA.class);
		Attribute attribute = entityType.getAttribute(property);
		return attribute;
	}

}
