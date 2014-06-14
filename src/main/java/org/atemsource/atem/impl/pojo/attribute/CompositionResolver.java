package org.atemsource.atem.impl.pojo.attribute;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.impl.common.attribute.collection.AbstractCollectionAttributeImpl;

public interface CompositionResolver {

	boolean isComposition(Attribute<?, ?> attribute);

}
