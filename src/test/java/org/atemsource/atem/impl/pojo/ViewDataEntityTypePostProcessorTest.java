/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.relation.SetAssociationAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.meta.ViewData;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/meta/pojo/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ViewDataEntityTypePostProcessorTest
{

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	@Test
	@Ignore
	public void test()
	{
		EntityType<?> viewEntityType = entityTypeRepository.getEntityType(ViewData.class);
		EntityType<?> entityType = entityTypeRepository.getEntityType(EntityWithView.class);
		EntityType<?> attributeEntityType = entityTypeRepository.getEntityType(Attribute.class);

		SetAssociationAttribute<?> viewsAttribute =
			(SetAssociationAttribute<?>) attributeEntityType.getIncomingAssociation(viewEntityType.getCode());
		List<String> attributeNamesInView = new ArrayList<String>();
		for (Attribute attribute : entityType.getAttributes())
		{
			Set associatedEntities = viewsAttribute.getValue(attribute);
			if (associatedEntities.size() > 0)
			{
				attributeNamesInView.add(attribute.getCode());
			}
		}
		Assert.assertEquals(1, attributeNamesInView.size());
	}
}
