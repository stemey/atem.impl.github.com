package org.atemsource.atem.impl.jsoup.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.OrderableCollection;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.type.primitive.TextType;
import org.atemsource.atem.impl.common.attribute.collection.ListAttributeImpl;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CssQueryAttributesAttributes implements OrderableCollection<String,List<String>> {
	private String attribute;
	private Type<String> textType;

	public CssQueryAttributesAttributes(Type<String> type,String cssQuery,String attribute) {
		super();
		this.cssQuery = cssQuery;
		this.attribute=attribute;
		this.textType=type;
	}

	private String cssQuery;

	public List<String> getValue(Object entity) {
		List<String> attributes = new ArrayList<String>();
		Elements elements = ((Element) entity).select(cssQuery);
		for (Element element: elements ) {
			attributes.add(element.attr(attribute));
		}
		return attributes;
	}

	@Override
	public void addElement(Object entity, String element) {
		throw new UnsupportedOperationException("cant be written to");
	}

	@Override
	public void clear(Object entity) {
		throw new UnsupportedOperationException("cant be written to");
	}

	@Override
	public boolean contains(Object entity, String element) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public CollectionSortType getCollectionSortType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getElements(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getEmptyCollection(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<String> getIterator(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize(Object entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeElement(Object entity, String element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<List<String>> getAssociationType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityType getEntityType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<List<String>> getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cardinality getTargetCardinality() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type<String> getTargetType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type<String> getTargetType(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type<String>[] getValidTargetTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isComposition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEqual(Object entity, Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWriteable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValue(Object entity, List<String> value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addElement(Object entity, int index, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getElement(Object entity, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIndex(Object entity, String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void moveElement(Object entity, int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object removeElement(Object entity, int index) {
		// TODO Auto-generated method stub
		return null;
	}


}
