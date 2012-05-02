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
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/meta/pojo/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class PrimitiveAttributeFactoryTest extends TestCase
{

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	@Test
	public void testIntegerProperty()
	{
		EntityType entityType = entityTypeRepository.getEntityType(EntityB.class);
		SingleAttribute attribute = (SingleAttribute) entityType.getAttribute("integer");
		Assert.assertNotNull(attribute);
		EntityB b = new EntityB();
		attribute.setValue(b, 1);
		Assert.assertEquals(1, attribute.getValue(b));
	}

}
