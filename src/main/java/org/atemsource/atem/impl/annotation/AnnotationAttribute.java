package org.atemsource.atem.impl.annotation;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.JavaMetaData;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.meta.DerivedObject;

public class AnnotationAttribute<J extends Annotation> implements SingleAttribute<J> {
	private EntityType attributeType;
	private EntityType<J> annotationType;

	@Inject
	private EntityTypeRepository entityTypeRepository;

	public AnnotationAttribute(EntityType<?> attributeType, EntityType<J> annotationType) {
		this.attributeType = attributeType;
		this.annotationType = annotationType;
	}

	public EntityType getEntityType() {
		return attributeType;
	}

	@Override
	public Class<J> getAssociationType() {
		return (Class<J>) annotationType.getJavaType();
	}

	@Override
	public Type<J>[] getValidTargetTypes() {
		return new Type[] { annotationType };
	}

	@Override
	public String getCode() {
		return annotationType.getJavaType().getName().replace(".", "_");
	}

	@Override
	public Class<J> getReturnType() {
		return (Class<J>) annotationType.getJavaType();
	}

	@Override
	public Cardinality getTargetCardinality() {
		return null;
	}

	@Override
	public Type<J> getTargetType() {
		return annotationType;
	}

	@Override
	public Type<J> getTargetType(J value) {
		return null;
	}

	@Override
	public J getValue(Object entity) {
		J value = null;
		if (entity instanceof JavaMetaData) {
			value = ((JavaMetaData) entity).getAnnotation(annotationType.getJavaType());
		}
		if (value == null) {
			Attribute metaAttribute = attributeType.getMetaAttribute(DerivedObject.META_ATTRIBUTE_CODE);
			if (metaAttribute != null) {
				DerivedObject derivedObject = (DerivedObject) metaAttribute.getValue(entity);
				if (derivedObject != null) {
					Object original = derivedObject.getOriginal();
					if (original != null) {
						return getValue(original);
					}
				}
			}

		}
		return value;
	}

	@Override
	public boolean isComposition() {
		return true;
	}

	@Override
	public boolean isEqual(Object entity, Object other) {
		J value = getValue(entity);
		J otherValue = getValue(other);
		if (value != null) {
			return value.equals(otherValue);
		} else {
			return otherValue == null;
		}

	}

	@Override
	public boolean isRequired() {
		return false;
	}

	@Override
	public boolean isWriteable() {
		return false;
	}

	@Override
	public void setValue(Object entity, J value) {
		throw new UnsupportedOperationException("cannot write annotation");
	}

	@Override
	public boolean isDerived() {
		return true;
	}

	@Override
	public void setMetaValue(String metaAttributeCode, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Attribute<?, ?> getMetaAttribute(String metaAttributeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityType<? extends Attribute<?, ?>> getMetaType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getMetaValue(String metaAttributeCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
