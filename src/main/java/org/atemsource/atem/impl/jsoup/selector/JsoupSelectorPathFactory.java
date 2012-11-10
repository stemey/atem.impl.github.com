package org.atemsource.atem.impl.jsoup.selector;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.path.AttributePath;
import org.atemsource.atem.api.path.AttributePathFactory;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.jsoup.nodes.Element;

public class JsoupSelectorPathFactory implements AttributePathFactory {

	@Inject
	private EntityTypeRepository entityTypeRepository;
	private EntityType<Element> elementType;
	private Type<String> textType;

	@PostConstruct
	public void initialize() {
		elementType = entityTypeRepository.getEntityType(Element.class);
		textType = entityTypeRepository.getType(String.class);
	}

	public AttributePath createAttributeSelector(Cardinality cardinality,
			String attribute, String cssQuery) {
		switch (cardinality) {
		case ONE:
		case ZERO_TO_ONE:
			return new JsoupSelectorPath(new CssQuerySingleAttributeAttributes(
					textType, elementType, attribute, cssQuery), "1::"
					+ cssQuery);
		case ZERO_TO_MANY:
		case MANY:
			return new JsoupSelectorPath(new CssQueryAttributesAttributes(
					textType, cssQuery, attribute), "*::" + cssQuery);
		default:
			throw new IllegalStateException("unknown cardinality "
					+ cardinality);
		}
	}

	public AttributePath createElementSelector(Cardinality cardinality,
			String cssQuery) {
		switch (cardinality) {
		case ONE:
		case ZERO_TO_ONE:
			return new JsoupSelectorPath(new CssQuerySingleElementAttributes(
					elementType, cssQuery), "1::" + cssQuery);
		case ZERO_TO_MANY:
		case MANY:
			return new JsoupSelectorPath(new CssQueryElementsAttributes(
					cssQuery, elementType), "*::" + cssQuery);
		default:
			throw new IllegalStateException("unknown cardinality "
					+ cardinality);
		}
	}

	@Override
	public AttributePath create(String path) {
		int firstColon = path.indexOf(':');
		String cardinality = path.substring(0, firstColon);
		int secondColon = path.substring(firstColon+1).indexOf(':')+firstColon+1;
		String attribute = path.substring(firstColon+1, secondColon);
		String cssQuery = path.substring(firstColon+secondColon);
		if (StringUtils.isEmpty(attribute)) {
			return createElementSelector(getCardinality(cardinality), cssQuery);
		} else {
			return createAttributeSelector(getCardinality(cardinality),
					attribute, cssQuery);
		}
	}

	private Cardinality getCardinality(String cardinality) {
		if (cardinality.equals("*")) {
			return Cardinality.ZERO_TO_MANY;
		} else if (cardinality.equals("+")) {
			return Cardinality.MANY;
		} else if (cardinality.equals("?")) {
			return Cardinality.ZERO_TO_ONE;
		} else if (cardinality.equals("1")) {
			return Cardinality.ONE;
		} else {
			throw new IllegalArgumentException("unknown cardinality "
					+ cardinality);
		}

	}

	@Override
	public AttributePath create(AttributePath basePath, String path) {
		// TODO Auto-generated method stub
		return null;
	}

}
