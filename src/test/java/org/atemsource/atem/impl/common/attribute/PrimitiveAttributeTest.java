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
public class PrimitiveAttributeTest extends AttributeTest
{

	private static String[] propertyNames = new String[]{"booleanO", "booleanP", "intO", "intP", "longO", "number"};

	@Autowired
	protected EntityTypeRepository entityTypeRepository;

	private EntityA createEntityA()
	{
		EntityA entity = new EntityA();
		entity.setBooleanO(Boolean.TRUE);
		entity.setBooleanP(false);
		entity.setIntO(null);
		entity.setIntP(12);
		entity.setLongO(4L);
		entity.setNumber(new Long(10));
		return entity;
	}

	private void getAndAssert(String propertyName, Object expectedValue, Object entity)
	{
		Attribute attribute = getAttribute(propertyName);
		Object value = attribute.getValue(entity);
		assertEquals(expectedValue, value);
	}

	private void setAndAssert(String propertyName, Object expectedValue, EntityA blankEntity)
	{
		SingleAttribute attribute = (SingleAttribute) getAttribute(propertyName);
		attribute.setValue(blankEntity, expectedValue);
		Object value = attribute.getValue(blankEntity);
		assertEquals(expectedValue, value);
	}

	@Test
	public void testAttributeType()
	{
		EntityA entity = new EntityA();
		for (String propertyName : propertyNames)
		{
			Assert.assertTrue("cannot find property " + propertyName, entityTypeRepository.getEntityType(entity)
				.getAttribute(propertyName) instanceof SingleAttribute);
		}
	}

	@Test
	public void testGetter()
	{
		EntityA entity = createEntityA();

		getAndAssert("booleanP", entity.isBooleanP(), entity);
		getAndAssert("booleanO", entity.getBooleanO(), entity);
		getAndAssert("intO", entity.getIntO(), entity);
		getAndAssert("intP", entity.getIntP(), entity);
		getAndAssert("longO", entity.getLongO(), entity);
		getAndAssert("number", entity.getNumber(), entity);

	}

	@Test
	public void testSetter()
	{
		EntityA entity = createEntityA();
		EntityA blankEntity = new EntityA();

		setAndAssert("booleanP", entity.isBooleanP(), blankEntity);
		setAndAssert("booleanO", entity.getBooleanO(), blankEntity);
		setAndAssert("intO", entity.getIntO(), blankEntity);
		setAndAssert("intP", entity.getIntP(), blankEntity);
		setAndAssert("longO", entity.getLongO(), blankEntity);
		setAndAssert("number", entity.getNumber(), blankEntity);

	}
}
