package org.atemsource.atem.impl.jsoup.attribute;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.MapAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;

public class ChildrenAttribute extends NodesAttribute {
	public ChildrenAttribute(EntityType nodeType) {
		super(nodeType);
	}

	protected List<Node> getValueInternally(Object entity) {
		return ((Node) entity).childNodes();
	}

}
