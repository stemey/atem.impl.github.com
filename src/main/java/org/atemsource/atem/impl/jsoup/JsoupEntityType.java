package org.atemsource.atem.impl.jsoup;

import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.AbstractEntityType;
import org.jsoup.nodes.Node;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class JsoupEntityType extends AbstractEntityType<Node> implements
		EntityType<Node> {

	@Override
	public Class<Node> getJavaType() {
		return Node.class;
	}

	@Override
	public boolean isEqual(Node a, Node b) {
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
		return value instanceof Node;
	}

}
