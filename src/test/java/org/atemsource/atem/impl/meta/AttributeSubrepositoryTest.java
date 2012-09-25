package org.atemsource.atem.impl.meta;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.OrderableCollection;
import org.atemsource.atem.api.attribute.relation.ListAssociationAttribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.type.primitive.ChoiceType;
import org.atemsource.atem.api.type.primitive.EnumType;
import org.atemsource.atem.impl.common.attribute.PrimitiveAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.ListAttributeImpl;
import org.atemsource.atem.impl.json.JsonEntityTypeImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:/test/atem/meta/attribute.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class AttributeSubrepositoryTest {

	@Inject
	private EntityTypeRepository entityTypeRepository;

	@Test
	public void testPrimitiveAttributeImplHierachy() {
		EntityType entityType = entityTypeRepository
				.getEntityType(PrimitiveAttributeImpl.class);
		Assert.assertNotNull(entityType);
		Assert.assertEquals(SingleAttribute.class, entityType.getEntityClass());
		Assert.assertEquals(Attribute.class, entityType.getSuperEntityType().getEntityClass());
	}
	
	@Test
	public void testListAttributeImplHierachy() {
		EntityType entityType = entityTypeRepository
				.getEntityType(ListAttributeImpl.class);
		Assert.assertNotNull(entityType);
		Assert.assertEquals(ListAssociationAttribute.class, entityType.getEntityClass());
		Assert.assertEquals(OrderableCollection.class, entityType.getSuperEntityType().getEntityClass());
		Assert.assertEquals(CollectionAttribute.class, entityType.getSuperEntityType().getSuperEntityType().getEntityClass());
		Assert.assertEquals(Attribute.class, entityType.getSuperEntityType().getSuperEntityType().getSuperEntityType().getEntityClass());
	}
}
