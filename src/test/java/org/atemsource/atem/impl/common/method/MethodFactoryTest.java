package org.atemsource.atem.impl.common.method;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.validation.constraints.Min;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.method.Method;
import org.atemsource.atem.api.type.EntityType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:/test/atem/method/entitytype.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class MethodFactoryTest {

	@Inject
	private MethodFactory methodFactory;
	
	@Test
	public void testInvoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = methodFactory.create(TestService.class.getDeclaredMethods()[0]);
		Object[] param = method.getParameterType().createEntity();
		Attribute<Integer,Integer> parameter = (Attribute<Integer, Integer>) method.getParameterType().getParameter(0);
		parameter.setValue(param,17);
		Object result=method.invoke(new TestService(),param);
		int count=(Integer) ((EntityType<Example>)method.getReturnType()).getAttribute("count").getValue(result);
		Assert.assertEquals(17,count);
	}

	@Test
	public void testAnnotation() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = methodFactory.create(TestService.class.getDeclaredMethods()[0]);
		Min min = (Min) method.getParameterType().getParameter(0).getMetaValue("javax_validation_constraints_Min");
		long count=min.value();
		Assert.assertEquals(10,count);
	}

	@Test
	public void testAttributeType() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = methodFactory.create(TestService.class.getDeclaredMethods()[0]);
		Attribute<?, ?> parameter = method.getParameterType().getParameter(0);
		Assert.assertTrue("was "+parameter.getClass().getName(),parameter instanceof SingleAttribute);
	}

}
