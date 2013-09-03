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
package org.atemsource.atem.impl.fasterjson;

import java.util.Map;

import javax.inject.Inject;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.MapAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:/test/atem/fasterjson/entitytype.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class JsonTypeImplTest {

	private static final String PROPERTY = "text";

	@Inject
	private EntityTypeRepository entityTypeRepository;
	

	@Inject
	private ObjectMapper objectMapper;

	private EntityType<ObjectNode> jsonType;

	private MapAttribute<String, Object, Map> propertiesAttribute;

	private EntityType<Object> arrayType;


	@Before
	public void setup() {
		jsonType = entityTypeRepository.getEntityType(ObjectNode.class
				.getName());
		propertiesAttribute = (MapAttribute<String, Object, Map>) jsonType
				.getAttribute("properties");
		arrayType = entityTypeRepository.getEntityType(ArrayNode.class
				.getName());
	}

	@Test
	public void testString() {
		ObjectNode root = objectMapper.createObjectNode();
		root.put(PROPERTY, "Hallo");
		Object text = propertiesAttribute.getElement(root, PROPERTY);
		Assert.assertEquals("Hallo", text);
		propertiesAttribute.putElement(root, PROPERTY, "Ciao");
		Assert.assertEquals("Ciao", root.get(PROPERTY).textValue());
		Type<Object> targetType = propertiesAttribute.getTargetType(text);
		Assert.assertEquals(entityTypeRepository.getType(String.class),targetType);

	}

	@Test
	public void testInt() {
		ObjectNode root = objectMapper.createObjectNode();
		root.put(PROPERTY, 100);
		Object value = propertiesAttribute.getElement(root, PROPERTY);
		Assert.assertEquals(100, value);
		propertiesAttribute.putElement(root, PROPERTY, 200);
		Assert.assertEquals(200, root.get(PROPERTY).intValue());
		Type<Object> targetType = propertiesAttribute.getTargetType(value);
		Assert.assertEquals(entityTypeRepository.getType(Integer.class),targetType);

	}

	@Test
	public void testArray() {
		ObjectNode root = objectMapper.createObjectNode();
		ArrayNode arrayNode = objectMapper.createArrayNode();
		arrayNode.add(100);
		arrayNode.add(200);
		root.put(PROPERTY, arrayNode);
		Object value = propertiesAttribute.getElement(root, PROPERTY);
		Assert.assertEquals(arrayNode, value);
		propertiesAttribute.putElement(root, PROPERTY, arrayNode);
		Assert.assertEquals(arrayNode, root.get(PROPERTY));

		Type<Object> targetType = propertiesAttribute.getTargetType(value);
		Assert.assertEquals(arrayType,targetType);

	}

	@Test
	public void testObject() {
		ObjectNode root = objectMapper.createObjectNode();
		ObjectNode child = objectMapper.createObjectNode();
		child.put("sub", 200);
		root.put(PROPERTY, child);
		ObjectNode value = (ObjectNode) propertiesAttribute.getElement(root,
				PROPERTY);
		Assert.assertEquals(value, child);
		propertiesAttribute.putElement(root, PROPERTY, child);
		Assert.assertEquals(child, root.get(PROPERTY));
		
		Type<Object> targetType = propertiesAttribute.getTargetType(value);
		Assert.assertEquals(jsonType,targetType);

	}

}
