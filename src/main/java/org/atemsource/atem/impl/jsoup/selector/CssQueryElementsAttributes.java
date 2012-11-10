package org.atemsource.atem.impl.jsoup.selector;

import java.util.List;

import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.jsoup.attribute.NodesAttribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CssQueryElementsAttributes extends NodesAttribute<Element> {
	public CssQueryElementsAttributes(String cssQuery, EntityType nodeType) {
		super(nodeType);
		this.cssQuery = cssQuery;
	}

	private String cssQuery;

	@Override
	protected List<Element> getValueInternally(Object entity) {
		return ((Element) entity).select(cssQuery);
	}

}
