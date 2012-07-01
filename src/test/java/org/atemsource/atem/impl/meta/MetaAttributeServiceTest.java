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
package org.atemsource.atem.impl.meta;


import javax.annotation.Resource;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.meta.MetaAttributeService;
import org.atemsource.atem.impl.meta.SingleMetaAttribute;
import org.atemsource.atem.impl.pojo.EntityB;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/atem/pojo/metaAttributeService.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MetaAttributeServiceTest
{

	@Autowired
	private EntityTypeRepository entityTypeRepository;

	@Resource
	private MetaAttributeService dynamicMetaAttributeService;

	@Test
	public void testSetMetaData()
	{
		EntityType<Attribute> holderType = entityTypeRepository.getEntityType(Attribute.class);
		EntityType<MetaDataExample> metaDataType = entityTypeRepository.getEntityType(MetaDataExample.class);
		EntityType<EntityB> entityType = entityTypeRepository.getEntityType(EntityB.class);
		Attribute attribute = entityType.getAttribute("singleA");
		SingleMetaAttribute<MetaDataExample> addMetaAttribute =
			dynamicMetaAttributeService.addSingleMetaAttribute("test", holderType, metaDataType);
		MetaDataExample ex = new MetaDataExample();
		ex.setData("hallo");
		addMetaAttribute.setValue(attribute, ex);

		Object value = addMetaAttribute.getValue(attribute);
		Assert.assertEquals(ex, value);
	}
}
