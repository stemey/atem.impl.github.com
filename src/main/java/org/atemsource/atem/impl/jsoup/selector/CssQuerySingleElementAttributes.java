package org.atemsource.atem.impl.jsoup.selector;

import java.util.List;

import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.type.primitive.TextType;
import org.atemsource.atem.impl.jsoup.attribute.NodesAttribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CssQuerySingleElementAttributes implements SingleAttribute<Element> {
	private EntityType<Element> elementType;
	public CssQuerySingleElementAttributes(EntityType<Element> elementType,String cssQuery) {
		super();
		this.cssQuery = cssQuery;
		this.elementType=elementType;
	}

	private String cssQuery;

	@Override
	public Class<Element> getAssociationType() {
		return Element.class;
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
	public Class<Element> getReturnType() {
		return Element.class;
	}

	@Override
	public Cardinality getTargetCardinality() {
		return null;
	}

	@Override
	public Type<Element> getTargetType() {
		return elementType;
	}

	@Override
	public Type<Element> getTargetType(Element value) {
		return elementType;
	}

	@Override
	public Type<Element>[] getValidTargetTypes() {
		return null;
	}

	@Override
	public Element getValue(Object entity) {
		Element element=(Element) entity;
		Elements elements = element.select(cssQuery);
		if (elements.size()==0) {
			return null;
		}else{
			return elements.get(0);
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
	public void setValue(Object entity, Element value) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	

}
