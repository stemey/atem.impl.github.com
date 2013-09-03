package org.atemsource.atem.impl.annotation;

import javax.inject.Inject;
import javax.validation.constraints.Max;

import junit.framework.Assert;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.annotation.domain.AnnotatedPojo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = {"classpath:/test/atem/annotation/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class AnnotationMetaAtributeServiceTest
{

	@Inject
	private EntityTypeRepository entityTypeRepository;

	@Test
	public void test()
	{
		EntityType<Attribute> attributeEntityType = entityTypeRepository.getEntityType(Attribute.class);
		Attribute annotationAttribute = attributeEntityType.getMetaAttribute(Max.class.getName());
		Attribute attribute = entityTypeRepository.getEntityType(AnnotatedPojo.class).getAttribute("maxedValue");
		Assert.assertNotNull(annotationAttribute);
		Object annotation = annotationAttribute.getValue(attribute);
		Assert.assertNotNull(annotation);
		long max =
			(Long) ((EntityType<?>) annotationAttribute.getTargetType()).getAttribute("value").getValue(annotation);
		Assert.assertEquals(23, max);
	}

	@Test
	public void testAnnotationType()
	{
		EntityType<?> annotationType = entityTypeRepository.getEntityType(Max.class);
		Assert.assertNotNull(annotationType);
		Assert.assertEquals(4, annotationType.getAttributes().size());
	}

}
