package org.atemsource.atem.impl.meta;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.type.primitive.ChoiceType;
import org.atemsource.atem.api.type.primitive.EnumType;
import org.atemsource.atem.impl.json.JsonEntityTypeImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:/test/atem/meta/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class EntityTypeSubrepositoryTest {

	
	@Inject
	private EntityTypeRepository entityTypeRepository;
	
	@Test
	public void testEnumTypeHierachy() {
		EntityType entityType=entityTypeRepository.getEntityType(EnumType.class);
		Assert.assertNotNull(entityType);
		Assert.assertEquals(ChoiceType.class,entityType.getSuperEntityType().getEntityClass());
		Assert.assertEquals(PrimitiveType.class,entityType.getSuperEntityType().getSuperEntityType().getEntityClass());
		Assert.assertEquals(Type.class,entityType.getSuperEntityType().getSuperEntityType().getSuperEntityType().getEntityClass());
	}
	@Test
	public void testEntityTypeHierachy() {
		EntityType entityType=entityTypeRepository.getEntityType(JsonEntityTypeImpl.class);
		Assert.assertNotNull(entityType);
		Assert.assertEquals(EntityType.class,entityType.getEntityClass());
	Assert.assertEquals(Type.class,entityType.getSuperEntityType().getEntityClass());
	}
}
