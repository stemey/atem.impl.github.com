package org.atemsource.atem.impl.infrastructure;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.atemsource.atem.api.BeanLocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:/test/atem/pojo/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BeanLocatorTest {

	@Test
	public void test() {
		Assert.assertNotNull(BeanLocator.getInstance());
	}

}
