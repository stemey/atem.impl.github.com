package org.atemsource.atem.impl.common.builder;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.SingleAttributeBuilder;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.SingleAbstractAttribute;

public class SingleAttributeBuilderImpl<T, B extends SingleAttributeBuilder<T, B>>
		implements SingleAttributeBuilder<T, B> {

	protected SingleAbstractAttribute<T> attribute;
	protected EntityTypeRepository entityTypeRepository;

	public SingleAttributeBuilderImpl(
			EntityTypeRepository entityTypeRepository,
			SingleAbstractAttribute<T> attribute) {
		super();
		this.attribute = attribute;
		this.entityTypeRepository = entityTypeRepository;
	}

	@Override
	public B required(boolean required) {
		attribute.setRequired(required);
		return (B) this;
	}

	@Override
	public B type(Type<T> targetType) {
		attribute.setTargetType(targetType);
		return (B) this;
	}

	@Override
	public B type(Class<T> javaType) {
		attribute.setTargetType(entityTypeRepository.getType(javaType));
		return (B) this;
	}

	@Override
	public SingleAttribute<T> create() {
		return attribute;
	}

}
