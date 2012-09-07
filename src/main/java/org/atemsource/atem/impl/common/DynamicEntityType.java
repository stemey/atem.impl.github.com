package org.atemsource.atem.impl.common;

import java.util.ArrayList;
import java.util.List;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.type.EntityType;

public abstract class DynamicEntityType<J> extends AbstractEntityType<J> {
	private List<EntityType<?>> mixinTypes = new ArrayList<EntityType<?>>();

	@Override
	public void addMixin(EntityType<?> mixinType) {
		mixinTypes.add(mixinType);
	}

	@Override
	public Attribute getDeclaredAttribute(String code) {
		Attribute declaredAttribute = super.getDeclaredAttribute(code);
		if (declaredAttribute != null) {
			return declaredAttribute;
		} else {
			for (EntityType<?> mixinType : mixinTypes) {
				declaredAttribute = mixinType.getDeclaredAttribute(code);
				if (declaredAttribute != null) {
					return declaredAttribute;
				}
			}
		}
		return null;
	}

	@Override
	public List<Attribute> getDeclaredAttributes() {
		List<Attribute> declaredAttributes = super.getDeclaredAttributes();
		if (mixinTypes.size() > 0) {
			List<Attribute> allDeclaredAttributes = new ArrayList<Attribute>();
			allDeclaredAttributes.addAll(declaredAttributes);
			for (EntityType<?> mixinType : mixinTypes) {
				allDeclaredAttributes.addAll(mixinType.getDeclaredAttributes());
			}
			return allDeclaredAttributes;
		} else {
			return declaredAttributes;
		}
	}

}
