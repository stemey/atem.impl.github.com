package org.atemsource.atem.impl.pojo.attribute;

import org.atemsource.atem.api.attribute.Attribute;

public interface TargetClassResolver {
	public Class<?> getCollectionElementClass(PropertyDescriptor propertyDescriptor);
	public Class<?> getMapKeyClass(PropertyDescriptor propertyDescriptor);
	public Class<?> getMapValueClass(PropertyDescriptor propertyDescriptor);
}
