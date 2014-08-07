package org.atemsource.atem.impl.common.builder;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.type.SingleAssociationAttributeBuilder;
import org.atemsource.atem.impl.common.attribute.SingleAssociationAttribute;

public class SingleAssociationAttributeBuilderImpl<T> extends SingleAttributeBuilderImpl<T,SingleAssociationAttributeBuilder<T>> implements SingleAssociationAttributeBuilder<T> {

	
	
	public SingleAssociationAttributeBuilderImpl(EntityTypeRepository entityTypeRepository,
			SingleAssociationAttribute<T> attribute) {
		super(entityTypeRepository,attribute);
	}


	@Override
	public SingleAssociationAttributeBuilder<T> cardinality(Cardinality cardinality) {
		attribute.setTargetCardinality(cardinality);
		return this;
	}

	@Override
	public SingleAssociationAttributeBuilder<T> composition(boolean composition) {
		attribute.setComposition(composition);
		return this;

	}

	

	
	
}

