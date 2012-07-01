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


import junit.framework.Assert;
import junit.framework.TestCase;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.attribute.SingleAssociationAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/atem/pojo/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SingleAssociationAttributeFactoryTest extends TestCase
{

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	@Test
	public void testSingleComposition()
	{
		EntityType entityType = entityTypeRepository.getEntityType(EntityB.class);
		SingleAssociationAttribute attribute = (SingleAssociationAttribute) entityType.getAttribute("singleA");
		Assert.assertNotNull(attribute);
		EntityA a = (EntityA) attribute.createEntity(attribute.getTargetType().getCode());
		Assert.assertNotNull(a);
	}

}
