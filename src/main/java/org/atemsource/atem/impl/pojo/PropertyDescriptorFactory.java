package org.atemsource.atem.impl.pojo;

import java.util.List;

import org.atemsource.atem.impl.pojo.attribute.PropertyDescriptor;

public interface PropertyDescriptorFactory {

	List<PropertyDescriptor> getPropertyDescriptors(Class entityClass);

}
