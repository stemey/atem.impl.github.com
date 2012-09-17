package org.atemsource.atem.impl.annotation;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.extension.MetaAttributeService;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.type.TypeFilter;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.atemsource.atem.spi.EntityTypeSubrepository;

public class AnnotationMetaAttributeService implements MetaAttributeService {

	@Override
	public <J> SingleAttribute<J> addSingleMetaAttribute(String name,
			EntityType<?> holderType, EntityType<J> metaDataType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <J> SingleAttribute<J> getMetaAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getMetaData(String name, Object holder) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private TypeFilter<?> annotationTypeFilter;

	@Override
	public void initialize(EntityTypeCreationContext ctx) {
		EntityType<?> attributeType = ctx
				.getEntityTypeReference(Attribute.class);
		for (EntityType<?> entityTypeReference : annotationTypeFilter.getEntityTypes()) {
			ctx.addMetaAttribute(attributeType, new AnnotationAttribute(
					attributeType,entityTypeReference));
		}
	}

	public TypeFilter<?> getAnnotationTypeFilter() {
		return annotationTypeFilter;
	}

	public void setAnnotationTypeFilter(TypeFilter<?> annotationTypeFilter) {
		this.annotationTypeFilter = annotationTypeFilter;
	}





}
