package org.atemsource.atem.impl.meta;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;

public class MetaAttribute<J> implements SingleAttribute<J> {
	private EntityType attributeType;
	private EntityType<J> targetType;

	@Inject
	private EntityTypeRepository entityTypeRepository;
	private MetaDataService metaDataService;
	private String code;

	public MetaAttribute(EntityType<?> attributeType, EntityType<J> targetType,
			MetaDataService metaDataService, String code) {
		this.attributeType = attributeType;
		this.targetType = targetType;
		this.metaDataService = metaDataService;
		this.code = code;
	}

	public EntityType getEntityType() {
		return attributeType;
	}

	@Override
	public Class<J> getAssociationType() {
		return (Class<J>) targetType.getJavaType();
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public Class<J> getReturnType() {
		return (Class<J>) targetType.getJavaType();
	}

	@Override
	public Cardinality getTargetCardinality() {
		return null;
	}

	@Override
	public Type<J> getTargetType() {
		return targetType;
	}
	@Override
	public Type<J>[] getValidTargetTypes() {
		return null;
	}
	@Override
	public Type<J> getTargetType(J value) {
		return targetType;
	}

	@Override
	public J getValue(Object entity) {
		J value = (J) metaDataService.getMetaData(entity, this);
		if (value == null) {
			Attribute metaAttribute = attributeType
					.getMetaAttribute(DerivedObject.META_ATTRIBUTE_CODE);
			if (metaAttribute == this) {
				return null;
			}
			if (metaAttribute != null) {
				DerivedObject derivedObject = (DerivedObject) metaAttribute
						.getValue(entity);
				if (derivedObject==null) {
					return null;
				}
				Object original = derivedObject.getOriginal();
				if (original != null) {
					return getValue(original);
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
		metaDataService.setMetaData(entity, value, this);
	}

}
