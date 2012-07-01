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


import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.relation.ListAssociationAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/atem/pojo/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ListAssociationAttributeTest extends AttributeTest
{

	private static final String PROPERTY = "list";

	@Autowired
	protected EntityTypeRepository entityTypeRepository;

	@Test
	public void testAttributeType()
	{
		EntityA entity = new EntityA();
		Assert
			.assertTrue(entityTypeRepository.getEntityType(entity).getAttribute(PROPERTY) instanceof ListAssociationAttribute);
	}

	@Test
	public void testGetter()
	{
		EntityA entity = new EntityA();
		final ArrayList<EntityB> list = new ArrayList<EntityB>();
		entity.setList(list);
		entity.getList().add(createEntity("Hallo", 2));
		entity.getList().add(createEntity("tst", 3));

		Attribute attribute = getAttribute(PROPERTY);
		Assert.assertNotNull(attribute);
		Object value = attribute.getValue(entity);
		assertEquals(list, value);
		Assert.assertTrue(list == value);

	}

	@Test
	public void testSetter()
	{
		EntityA entity = new EntityA();
		final ArrayList<EntityB> list = new ArrayList<EntityB>();
		list.add(createEntity("Hallo", 2));
		list.add(createEntity("tst", 3));

		ListAssociationAttribute<?> attribute = (ListAssociationAttribute<?>) getAttribute(PROPERTY);
		Assert.assertNotNull(attribute);
		attribute.setValue(entity, list);
		List<EntityB> value = entity.getList();
		assertEquals(list, value);
		Assert.assertTrue(list == value);
	}
}
