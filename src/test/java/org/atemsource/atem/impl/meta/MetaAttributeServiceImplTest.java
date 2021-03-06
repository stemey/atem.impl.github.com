/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.meta;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.extension.MetaAttributeService;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.pojo.EntityB;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/atem/pojo/metaAttributeService.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MetaAttributeServiceImplTest
{

	@Resource
	private MetaAttributeService dynamicMetaAttributeService;

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	@Test
	public void testGetMetaValue()
	{
		// setup meta attribute
		EntityType<Attribute> holderType = entityTypeRepository.getEntityType(Attribute.class);
		EntityType<MetaDataExample> metaDataType = entityTypeRepository.getEntityType(MetaDataExample.class);
		SingleAttribute<MetaDataExample> testMetaAttribute =
			dynamicMetaAttributeService.addSingleMetaAttribute("test2", holderType, metaDataType);

		// work with meta attribute
		EntityType<EntityB> entityType = entityTypeRepository.getEntityType(EntityB.class);
		Attribute attribute = entityType.getAttribute("singleA");

		MetaDataExample ex = new MetaDataExample();
		ex.setData("hallo");
		attribute.setMetaValue("test2", ex);
		Object metaValue = attribute.getMetaValue("test2");

		Assert.assertEquals(ex, metaValue);

	}

	@Test
	public void testSetMetaData()
	{
		// setup meta attribute
		EntityType<Attribute> holderType = entityTypeRepository.getEntityType(Attribute.class);
		EntityType<MetaDataExample> metaDataType = entityTypeRepository.getEntityType(MetaDataExample.class);
		SingleAttribute<MetaDataExample> testMetaAttribute =
			dynamicMetaAttributeService.addSingleMetaAttribute("test", holderType, metaDataType);

		// work with meta attribute
		EntityType<EntityB> entityType = entityTypeRepository.getEntityType(EntityB.class);
		Attribute attribute = entityType.getAttribute("singleA");
		MetaDataExample ex = new MetaDataExample();
		ex.setData("hallo");
		Attribute metaAttribute = holderType.getMetaAttribute("test");
		metaAttribute.setValue(attribute, ex);

		Assert.assertNotNull(metaAttribute);
		Assert.assertEquals(holderType, holderType);
		Object value = metaAttribute.getValue(attribute);
		Assert.assertEquals(ex, value);

	}
}
