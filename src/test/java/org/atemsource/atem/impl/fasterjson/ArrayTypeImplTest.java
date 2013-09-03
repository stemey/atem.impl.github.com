/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.fasterjson;

import javax.annotation.Resource;
import javax.inject.Inject;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.EntityTypeBuilder;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.fasterjson.attribute.ChildrenAttribute;
import org.atemsource.atem.spi.DynamicEntityTypeSubrepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/atem/fasterjson/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ArrayTypeImplTest
{

	private EntityType<Object> arrayType;

	private ChildrenAttribute childrenAttribute;

	@Inject
	private EntityTypeRepository entityTypeRepository;

	@Resource(name = "atem-fasterjson-repository")
	private DynamicEntityTypeSubrepository<JsonNode> jsonRepository;

	@Inject
	private ObjectMapper objectMapper;

	@Before
	public void setup()
	{
		arrayType = entityTypeRepository.getEntityType(ArrayNode.class.getName());
		childrenAttribute = (ChildrenAttribute) arrayType.getAttribute("children");
	}

	@Test
	public void testArray()
	{
		ArrayNode root = objectMapper.createArrayNode();
		ArrayNode arrayNode = objectMapper.createArrayNode();
		arrayNode.add(100);
		arrayNode.add(200);
		root.add(arrayNode);
		Object value = childrenAttribute.getElement(root, 0);
		Assert.assertEquals(arrayNode, value);
		childrenAttribute.addElement(root, arrayNode);
		Assert.assertEquals(arrayNode, root.get(1));

		Type<Object> targetType = childrenAttribute.getTargetType(value);
		Assert.assertEquals(arrayType, targetType);

	}

	@Test
	public void testInt()
	{
		ArrayNode root = objectMapper.createArrayNode();
		root.add(100);
		Object value = childrenAttribute.getElement(root, 0);
		Assert.assertEquals(100, value);
		childrenAttribute.addElement(root, 200);
		Assert.assertEquals(200, root.get(1).intValue());
		Type<Object> targetType = childrenAttribute.getTargetType(value);
		Assert.assertEquals(entityTypeRepository.getType(Integer.class), targetType);

	}

	@Test
	public void testObject()
	{
		ArrayNode root = objectMapper.createArrayNode();
		ObjectNode child = objectMapper.createObjectNode();
		child.put("sub", 200);
		root.add(child);
		ObjectNode value = (ObjectNode) childrenAttribute.getElement(root, 0);
		Assert.assertEquals(value, child);
		childrenAttribute.addElement(root, child);
		Assert.assertEquals(child, root.get(1));

		Type<Object> targetType = childrenAttribute.getTargetType(value);

	}

	@Test
	public void testString()
	{
		ArrayNode root = objectMapper.createArrayNode();
		root.add("Hallo");
		Object text = childrenAttribute.getElement(root, 0);
		Assert.assertEquals("Hallo", text);
		childrenAttribute.addElement(root, "Ciao");
		Assert.assertEquals("Ciao", root.get(1).textValue());
		Type<Object> targetType = childrenAttribute.getTargetType(text);
		Assert.assertEquals(entityTypeRepository.getType(String.class), targetType);

	}
}
