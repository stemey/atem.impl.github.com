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

import org.atemsource.atem.api.attribute.relation.ListAssociationAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/atem/pojo/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ListAssociationAttributeTest extends AbstractCollectionTest
{

	@Test
	public void testMoveAssociatedEntityAndGetIndex()
	{
		ListAssociationAttribute attribute = (ListAssociationAttribute) getCollectionAssociationAttribute();

		EntityA a = new EntityA();
		a.setList(new ArrayList<EntityB>());
		attribute.addElement(a, new EntityB());
		final EntityB b = new EntityB();
		attribute.addElement(a, b);
		Assert.assertEquals(1, attribute.getIndex(a, b));
		attribute.moveElement(a, 1, 0);
		Assert.assertEquals(0, attribute.getIndex(a, b));
	}
}
