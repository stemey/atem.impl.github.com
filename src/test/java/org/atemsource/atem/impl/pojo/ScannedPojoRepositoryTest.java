/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo;

import javax.inject.Inject;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.EntityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/atem/pojo/scanned-entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ScannedPojoRepositoryTest
{

	@Inject
	protected EntityTypeRepository entityTypeRepository;

	@Test
	public void testTypes()
	{
		EntityType<EntityA> entityType = entityTypeRepository.getEntityType(EntityA.class);
		Assert.assertNotNull(entityType);
		Assert.assertNotNull(entityType.getSubEntityTypes(true));
		Assert.assertEquals(1, entityType.getSubEntityTypes(true).size());
		Assert.assertEquals(4, entityType.getDeclaredAttributes().size());
		Assert.assertNotNull(entityType.getAttribute("list").getTargetType());

	}

}
