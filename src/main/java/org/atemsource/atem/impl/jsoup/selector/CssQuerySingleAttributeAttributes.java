package org.atemsource.atem.impl.jsoup.selector;

import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.type.primitive.TextType;
import org.jsoup.nodes.Element;

public class CssQuerySingleAttributeAttributes extends CssQuerySingleTextAttributes  {
	private String attribute;

	public CssQuerySingleAttributeAttributes(Type<String> textType,
			EntityType<Element> elementType, String attribute, String cssQuery) {
		super();
		this.cssQuery = cssQuery;
		this.elementType = elementType;
		this.textType = textType;
		this.attribute = attribute;
	}

	protected String getValueInternally(Element targetElement) {
		return targetElement.attr(attribute);
	}

}
