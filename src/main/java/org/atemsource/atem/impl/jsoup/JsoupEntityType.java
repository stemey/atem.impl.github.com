package org.atemsource.atem.impl.jsoup;

import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class JsoupEntityType extends AbstractEntityType<Element> implements
		EntityType<Element> {

	public JsoupEntityType() {
		super();
		setEntityClass(Element.class);
		setCode(Element.class.getName());
	}

	@Override
	public Class<Element> getJavaType() {
		return Element.class;
	}

	@Override
	public boolean isEqual(Element a, Element b) {
		if (a == null && b == null) {
			return true;
		} else if (a != null && b == null) {
			return false;
		} else if (a == null && b != null) {
			return false;
		} else {
			return a.equals(b);
		}
	}

	@Override
	public boolean isInstance(Object value) {
		return value instanceof Element;
	}

}
