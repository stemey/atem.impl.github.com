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
package org.atemsource.atem.impl.dynamic;

import javax.inject.Inject;

import junit.framework.Assert;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.EntityTypeBuilder;
import org.atemsource.atem.api.type.MultiAssociationAttributeBuilder;
import org.atemsource.atem.api.type.SingleAssociationAttributeBuilder;
import org.atemsource.atem.api.type.Type;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:/test/atem/dynamic/entitytype.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class DynamicEntityTypeImplTest {

	@Inject
	private DynamicEntityTypeRepository repository;

	@Test
	public void testString() {
		EntityTypeBuilder builder = repository.createBuilder("test");
		SingleAttribute<String> attribute = builder.addSingleAttribute("text",
				String.class);
		EntityType<?> entityType = builder.createEntityType();

		Object entity = entityType.createEntity();
		attribute.setValue(entity, "hallo");
		Assert.assertEquals("hallo", attribute.getValue(entity));
	}

	@Test
	public void testEmbedded() {

		EntityTypeBuilder embeddedBuilder = repository.createBuilder("text1");
		embeddedBuilder.addSingleAttribute("text", String.class);
		EntityType<?> embeddedType = embeddedBuilder.createEntityType();

		EntityTypeBuilder builder = repository.createBuilder("embedded");
		SingleAssociationAttributeBuilder<Object> attributeBuilder = builder
				.addSingleAssociationAttribute("embedded");
		attributeBuilder.composition(true);
		attributeBuilder.type((Type<Object>) embeddedType);
		Attribute<?, Object> attribute = attributeBuilder.create();

		EntityType<?> entityType = builder.createEntityType();

		Object entity = entityType.createEntity();
		DynamicEntityImpl data = new DynamicEntityImpl();
		attribute.setValue(entity, data);
		Assert.assertEquals(data, attribute.getValue(entity));
	}

	@Test
	public void testArray() {

		EntityTypeBuilder embeddedBuilder = repository.createBuilder("text2");
		embeddedBuilder.addSingleAttribute("text", String.class);
		EntityType<?> embeddedType = embeddedBuilder.createEntityType();

		EntityTypeBuilder builder = repository.createBuilder("array");
		CollectionAttribute attribute = builder.addMultiAssociationAttribute(
				"array", embeddedType, CollectionSortType.ORDERABLE);

		EntityType<DynamicEntity> entityType = (EntityType<DynamicEntity>) builder
				.createEntityType();

		Object entity = entityType.createEntity();
		Object data = new DynamicEntityImpl();
		attribute.addElement(entity, data);
		Assert.assertEquals(1, attribute.getElements(entity).size());
	}

}
