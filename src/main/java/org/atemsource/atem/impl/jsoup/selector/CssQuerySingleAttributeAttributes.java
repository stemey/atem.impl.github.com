package org.atemsource.atem.impl.jsoup.selector;

import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.type.primitive.TextType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CssQuerySingleAttributeAttributes implements
		SingleAttribute<String> {
	private EntityType<Element> elementType;
	private Type<String> textType;
	private String attribute;

	public CssQuerySingleAttributeAttributes(Type<String> textType,
			EntityType<Element> elementType, String attribute, String cssQuery) {
		super();
		this.cssQuery = cssQuery;
		this.elementType = elementType;
		this.textType = textType;
		this.attribute = attribute;
	}

	private String cssQuery;

	@Override
	public Class<String> getAssociationType() {
		return String.class;
	}

	@Override
	public String getCode() {
		return cssQuery;
	}

	@Override
	public EntityType getEntityType() {
		return elementType;
	}

	@Override
	public Class<String> getReturnType() {
		return String.class;
	}

	@Override
	public Cardinality getTargetCardinality() {
		return null;
	}

	@Override
	public Type<String> getTargetType() {
		return textType;
	}

	@Override
	public Type<String> getTargetType(String value) {
		return textType;
	}

	@Override
	public Type<String>[] getValidTargetTypes() {
		return null;
	}

	@Override
	public String getValue(Object entity) {
		Element element = (Element) entity;
		Elements elements = element.select(cssQuery);
		if (elements.size() == 0) {
			return null;
		} else {
			Element targetElement = elements.get(0);
			return targetElement.attr(attribute);
		}
	}

	@Override
	public boolean isComposition() {
		return false;
	}

	@Override
	public boolean isEqual(Object entity, Object other) {
		return false;
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
	public void setValue(Object entity, String value) {
		throw new UnsupportedOperationException("not implemented yet");
	}

}
