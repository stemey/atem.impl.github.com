package org.atemsource.atem.impl.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.atemsource.atem.impl.pojo.PropertyDescriptorFactory;
import org.atemsource.atem.impl.pojo.attribute.PropertyDescriptor;

public class AnnotationPropertyDescriptorFactory implements
		PropertyDescriptorFactory {

	@Override
	public List<PropertyDescriptor> getPropertyDescriptors(Class entityClass) {
		List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();
		for (Method method : entityClass.getDeclaredMethods()) {
			propertyDescriptors.add(new PropertyDescriptor(method.getName(),
					new AnnotationAccessor(method), method.getReturnType(),
					false,false));
		}
		return propertyDescriptors;
	}

}
