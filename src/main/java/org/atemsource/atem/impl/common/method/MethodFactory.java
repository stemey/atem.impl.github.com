package org.atemsource.atem.impl.common.method;

import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.impl.pojo.attribute.AttributeFactory;
import org.atemsource.atem.impl.pojo.attribute.PropertyDescriptor;
import org.atemsource.atem.spi.EntityTypeCreationContext;

public class MethodFactory {

	public List<AttributeFactory> getAttributeFactories() {
		return attributeFactories;
	}

	public void setAttributeFactories(List<AttributeFactory> attributeFactories) {
		this.attributeFactories = attributeFactories;
	}

	private List<AttributeFactory> attributeFactories;

	public org.atemsource.atem.api.method.Method create(Method method) {

		ParameterTypeImpl parameterType = new ParameterTypeImpl();
		parameterType.setCode(method.toGenericString());
		for (int index = 0; index < method.getParameterTypes().length; index++) {
			Attribute<?, ?> parameter = create(parameterType, index, method);
			if (parameter != null) {
				parameterType.addAttribute(parameter);
			}
		}
		MethodImpl atemMethod = new MethodImpl();
		atemMethod.setParameterType(parameterType);
		atemMethod.setReturnType(entityTypeRepository.getType(method.getReturnType()));
		atemMethod.setMethod(method);
		return atemMethod;
	}

	@Inject
	private EntityTypeRepository entityTypeRepository;

	public Attribute<?, ?> create(ParameterTypeImpl type, int index, Method method) {
		for (AttributeFactory attributeFactory : attributeFactories) {
			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(String.valueOf(index),
					new ParameterAccessor(method, index), method.getParameterTypes()[index], true, false);
			if (attributeFactory.canCreate(propertyDescriptor, (EntityTypeCreationContext) entityTypeRepository)) {
				return attributeFactory.createAttribute(type, propertyDescriptor,
						(EntityTypeCreationContext) entityTypeRepository);
			}
		}
		return null;
	}
}
