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


import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/meta/pojo/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SingleAssociationAttributeTest extends AttributeTest
{

	private static final String PROPERTY = "entityB";

	@Autowired
	protected EntityTypeRepository entityTypeRepository;

	@Test
	public void testAttributeType()
	{
		EntityA entity = new EntityA();
		Assert.assertTrue(entityTypeRepository.getEntityType(entity).getAttribute(PROPERTY) instanceof SingleAttribute);
	}

	@Test
	public void testGetter()
	{
		EntityA entity = new EntityA();
		final EntityB entityB = createEntity("Hallo", 2);
		entity.setEntityB(entityB);

		Attribute attribute = getAttribute(PROPERTY);
		Object value = attribute.getValue(entity);
		assertEquals(entity.getEntityB(), value);
		Assert.assertTrue(entity.getEntityB() == value);
	}

	@Test
	public void testSerialisation()
	{
		EntityA entity = new EntityA();
		final EntityB entityB = createEntity("Hallo", 2);
		entity.setEntityB(entityB);
	}

	@Test
	public void testSerialisationNull()
	{
		EntityA entity = new EntityA();
		entity.setEntityB(null);
	}

	@Test
	public void testSetter()
	{
		EntityA entity = new EntityA();
		final EntityB entityB = createEntity("Hallo", 2);

		SingleAttribute<EntityB> attribute = (SingleAttribute<EntityB>) getAttribute(PROPERTY);
		Assert.assertNotNull(attribute);
		attribute.setValue(entity, entityB);
		EntityB value = entity.getEntityB();
		assertEquals(entityB, value);
		Assert.assertTrue(entityB == value);
	}
}
