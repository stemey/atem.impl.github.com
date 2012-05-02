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
package org.atemsource.atem.impl.pojo;


import java.util.ArrayList;

import junit.framework.Assert;

import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.relation.ListAssociationAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/meta/pojo/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CollectionAssociationAttributeTest extends AbstractCollectionTest
{

	@Test
	public void isEqual()
	{
		EntityA a1 = new EntityA();
		a1.setList(new ArrayList<EntityB>());
		final EntityB b1 = new EntityB();
		a1.getList().add(b1);
		b1.setInteger(1);

		EntityA a2 = new EntityA();
		a2.setList(new ArrayList<EntityB>());
		final EntityB b2 = new EntityB();
		a2.getList().add(b2);
		b2.setInteger(3);

		EntityType entityType = entityTypeRepository.getEntityType(EntityA.class);
		ListAssociationAttribute attribute = (ListAssociationAttribute) entityType.getAttribute("list");
		Assert.assertFalse(attribute.isEqual(a1, a2));
		b2.setInteger(1);
		Assert.assertTrue(attribute.isEqual(a1, a2));
	}

	@Test
	public void testAddAssociatedEntity()
	{
		CollectionAttribute attribute = getCollectionAssociationAttribute();
		EntityA a = new EntityA();
		a.setList(new ArrayList<EntityB>());
		attribute.addElement(a, new EntityB());
		Assert.assertEquals(attribute.getSize(a), 1);
	}

	@Test
	public void testClearAssociatedEntities()
	{
		CollectionAttribute attribute = getCollectionAssociationAttribute();
		EntityA a = createA();
		attribute.clear(a);
		Assert.assertEquals(attribute.getSize(a), 0);
	}

	@Test
	public void testGetAssociatedEntities()
	{
		CollectionAttribute<?, ?> attribute = getCollectionAssociationAttribute();
		EntityA a = createA();
		Assert.assertEquals(attribute.getSize(a), 1);
	}

	@Test
	public void testGetAssociatedEntityCount()
	{
		CollectionAttribute attribute = getCollectionAssociationAttribute();
		EntityA a = createA();
		Assert.assertEquals(attribute.getSize(a), 1);
	}

	@Test
	public void testGetTargeType()
	{
		CollectionAttribute attribute = getCollectionAssociationAttribute();
		Assert.assertEquals(EntityB.class, (attribute.getTargetType()).getJavaType());
	}

	@Test
	public void testRemoveAssociatedEntity()
	{
		CollectionAttribute attribute = getCollectionAssociationAttribute();
		EntityA a = createA();
		EntityB b2 = new EntityB();
		a.getList().add(0, b2);
		attribute.removeElement(a, b2);
		Assert.assertEquals(1, a.getList().size());
	}

}
