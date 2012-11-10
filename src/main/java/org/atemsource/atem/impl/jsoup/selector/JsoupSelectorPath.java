package org.atemsource.atem.impl.jsoup.selector;

import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.path.AttributePath;
import org.atemsource.atem.api.path.Cardinality;
import org.atemsource.atem.api.path.PathType;
import org.atemsource.atem.api.type.Type;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * 
 * 
 * 
 * jsoup selector returns a list ofelements. th path should be able to return
 * attribute and elements as list or single elements.
 * 
 * Additonal information necessary:
 * 
 * - expected cardinality - attributeName
 * 
 * -> *:class:#mylist li return alls class attributes on the list element
 * descendants of element with id mylist.
 * 
 * -> 1:class:#mylist li or ?:class:#mylist li returns one class attribute. It
 * throws an exception if there are more than one.
 * 
 * -> *::#mylist li returns the list of lis
 * 
 * 
 * 
 * @author eee
 * 
 */

public class JsoupSelectorPath implements AttributePath {

	private Attribute attribute;
	private String selector;

	@Override
	public String getAsString() {
		return selector;
	}

	public JsoupSelectorPath(Attribute attribute, String selector) {
		super();
		this.attribute = attribute;
		this.selector = selector;
	}

	@Override
	public Attribute getAttribute() {
		return attribute;
	}

}
