package org.atemsource.atem.impl.jsoup.attribute;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.atemsource.atem.api.attribute.MapAttribute;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;

public class AttributesAttribute implements
		MapAttribute<String, String, Attributes> {

	@Override
	public Class<Attributes> getAssociationType() {
		return Attributes.class;
	}

	@Override
	public String getCode() {
		return "attributes";
	}

	@Override
	public EntityType getEntityType() {
		return entityType;
	}

	@Override
	public Class<Attributes> getReturnType() {
		return Attributes.class;
	}

	@Override
	public Cardinality getTargetCardinality() {
		return Cardinality.ZERO_TO_MANY;
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
	public Attributes getValue(Object entity) {
		return ((Node) entity).attributes();
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
			return ((Node) entity).attributes().equals(
					((Node) other).attributes());
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
	public void setValue(Object entity, Attributes value) {
		clear(entity);
		((Node) entity).attributes().addAll(value);
	}

	@Override
	public void clear(Object entity) {
		Attributes attributes = ((Node) entity).attributes();
		for (Iterator<Attribute> iterator = attributes.iterator();; iterator
				.hasNext()) {
			Attribute next = iterator.next();
			attributes.remove(next.getKey());
		}
	}

	@Override
	public boolean containsValue(Object entity, String element) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public String getElement(Object entity, String keye) {
		return ((Node) entity).attributes().get(keye);
	}

	@Override
	public Attributes getEmptyMap() {
		return new Attributes();
	}

	@Override
	public Iterator<Entry<?, ?>> getIterator(Object entity) {
		return (Iterator<Entry<?, ?>>) ((Node) entity).attributes().dataset()
				.entrySet();
	}

	@Override
	public Set<String> getKeySet(Object entity) {
		return ((Node) entity).attributes().dataset().keySet();
	}

	@Override
	public Type<String> getKeyType() {
		return textType;
	}

	@Override
	public Type<String> getKeyType(String key) {
		return textType;
	}

	private Type<String> textType;
	private EntityType entityType;

	@Override
	public int getSize(Object entity) {
		return ((Node) entity).attributes().size();
	}

	@Override
	public boolean isSorted() {
		return false;
	}

	@Override
	public void putElement(Object entity, String key, String value) {
		((Node) entity).attr(key, value);
	}

	@Override
	public void removeKey(Object entity, String key) {
		((Node) entity).removeAttr(key);
	}

	@Override
	public void removeValue(Object entity, String value) {
		throw new UnsupportedOperationException("notimplemented yet");
	}

	public Type<String> getTextType() {
		return textType;
	}

	public void setTextType(Type<String> textType) {
		this.textType = textType;
	}

	public AttributesAttribute(Type<String> textType, EntityType entityType) {
		super();
		this.textType = textType;
		this.entityType = entityType;
	}



}
