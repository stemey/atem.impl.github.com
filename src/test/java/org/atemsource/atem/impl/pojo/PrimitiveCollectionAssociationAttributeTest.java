/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo;

import java.util.ArrayList;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/atem/pojo/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class PrimitiveCollectionAssociationAttributeTest
{

	@Resource
	private EntityTypeRepository entityTypeRepository;

	@Test
	public void testGetCount()
	{
		EntityType entityType = entityTypeRepository.getEntityType(EntityA.class);
		CollectionAttribute attribute = (CollectionAttribute) entityType.getAttribute("objectList");
		Assert.assertNotNull(attribute);
		EntityA a = new EntityA();
		a.setObjectList(new ArrayList());
		a.getObjectList().add(new Object());
		a.getObjectList().add(new Object());
		Assert.assertEquals(attribute.getSize(a), 2);
	}

	@Test
	public void testTargetType()
	{
		EntityType entityType = entityTypeRepository.getEntityType(EntityA.class);
		CollectionAttribute attribute = (CollectionAttribute) entityType.getAttribute("stringList");
		Assert.assertNotNull(attribute);
		Assert.assertEquals(String.class, attribute.getTargetType().getJavaType());
	}
}
