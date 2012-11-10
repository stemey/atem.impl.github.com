package org.atemsource.atem.impl.jsoup.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.jsoup.nodes.Node;

public abstract  class NodesAttribute<N extends Node> implements CollectionAttribute<N, List>{

	private EntityType nodeType;

	@Override
	public Class<List> getAssociationType() {
		return List.class;
	}

	@Override
	public String getCode() {
		return "children";
	}



	@Override
	public EntityType getEntityType() {
		return nodeType;
	}

	@Override
	public Class<List> getReturnType() {
		return List.class;
	}

	@Override
	public Cardinality getTargetCardinality() {
		return Cardinality.ZERO_TO_MANY;
	}

	@Override
	public Type<N> getTargetType() {
		return nodeType;
	}

	@Override
	public Type<N> getTargetType(N value) {
		return nodeType;
	}

	@Override
	public Type<N>[] getValidTargetTypes() {
		return null;
	}

	@Override
	public List<N> getValue(Object entity) {
		return getValueInternally(entity);
	}

	protected abstract List<N> getValueInternally(Object entity) ;

	public EntityType getNodeType() {
		return nodeType;
	}



	public NodesAttribute(EntityType nodeType) {
		super();
		this.nodeType = nodeType;
	}

	@Override
	public boolean isComposition() {
		return false;
	}

	@Override
	public boolean isEqual(Object entity, Object other) {
		if (entity == null && other != null) {
			return false;
		} else if (entity != null && other == null) {
			return false;
		} else if (entity == null && other == null) {
			return true;
		} else {
			return getValueInternally(entity).equals(
					getValueInternally(other));
		}
	}

	@Override
	public boolean isRequired() {
		return false;
	}

	@Override
	public boolean isWriteable() {
		return true;
	}

	@Override
	public void setValue(Object entity, List value) {
		getValueInternally(entity).clear();
		if (value != null) {
			for (Object node : value) {
				getValueInternally(entity).add((N) node);
			}
		}
	}

	@Override
	public void addElement(Object entity, N element) {
		getValueInternally(entity).add(element);
	}

	@Override
	public void clear(Object entity) {
		getValueInternally(entity).clear();
	}

	@Override
	public boolean contains(Object entity, N element) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public CollectionSortType getCollectionSortType() {
		return CollectionSortType.ORDERABLE;
	}

	@Override
	public Collection<N> getElements(Object entity) {
		return getValueInternally(entity);
	}

	@Override
	public List<Node> getEmptyCollection(Object entity) {
		return new ArrayList<Node>();
	}

	@Override
	public Iterator<N> getIterator(Object entity) {
		return getValueInternally(entity).iterator();
	}

	@Override
	public int getSize(Object entity) {
		return getValueInternally(entity).size();
	}

	@Override
	public void removeElement(Object entity, Node element) {
		throw new UnsupportedOperationException("not implemented yet");
	}

}