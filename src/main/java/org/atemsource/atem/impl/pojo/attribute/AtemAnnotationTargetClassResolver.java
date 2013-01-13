package org.atemsource.atem.impl.pojo.attribute;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.annotation.Association;

public class AtemAnnotationTargetClassResolver implements TargetClassResolver{

	@Override
	public Class<?> getCollectionElementClass(PropertyDescriptor propertyDescriptor) {
		 Association association = propertyDescriptor.getAnnotation(Association.class);
		 if (association!=null) {
			 return association.targetType();
		 }else{
			 return null;
		 }
	}

	@Override
	public Class<?> getMapKeyClass(PropertyDescriptor propertyDescriptor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getMapValueClass(PropertyDescriptor propertyDescriptor) {
		// TODO Auto-generated method stub
		return null;
	}

}
