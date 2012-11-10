package org.atemsource.atem.impl.jsoup.attribute;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.jsoup.select.Elements;

public class AnyCollectionAttribute<J> implements CollectionAttribute<J, Elements>{

	@Override
	public Class<Elements> getAssociationType() {
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
	public Class<Elements> getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cardinality getTargetCardinality() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type<J> getTargetType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type<J> getTargetType(J value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type<J>[] getValidTargetTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Elements getValue(Object entity) {
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
	public void setValue(Object entity, Elements value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addElement(Object entity, J element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Object entity, J element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CollectionSortType getCollectionSortType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<J> getElements(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Elements getEmptyCollection(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<J> getIterator(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize(Object entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeElement(Object entity, J element) {
		// TODO Auto-generated method stub
		
	}



}
