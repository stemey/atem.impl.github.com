package org.atemsource.atem.impl.jsoup.selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.type.primitive.TextType;
import org.jsoup.nodes.Element;

public class CssQuerySingleMethodAttributes extends
		CssQuerySingleTextAttributes {
	private Method method;

	public CssQuerySingleMethodAttributes(Type<String> textType,
			EntityType<Element> elementType, String methodName, String cssQuery) {
		super();
		this.cssQuery = cssQuery;
		this.elementType = elementType;
		this.textType = textType;
		try {
			this.method = Element.class.getMethod(methodName);
			if (method.getReturnType() != String.class) {
				throw new IllegalArgumentException("method " + methodName
						+ " does not return an instance of String");
			}
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("method " + methodName
					+ " not found on Element");
		} catch (SecurityException e) {
			throw new IllegalArgumentException("method " + methodName
					+ " not found on Element");
		}
	}

	protected String getValueInternally(Element targetElement) {
		try {
			return (String) method.invoke(targetElement, new Object[0]);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
