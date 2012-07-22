package org.atemsource.atem.impl.pojo.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SingleViewIndex {

	private ViewCreator viewCreator;

	private Map<Serializable, Object> index=new HashMap<Serializable, Object>();

	public SingleViewIndex(ViewCreator viewCreator) {
		this.viewCreator = viewCreator;
	}

	public void create(Collection<Object> entities) {
		for (Object entity : entities) {
			insert(entity);
		}
	}

	public Object find(Object[] parameters) {
		Serializable key = viewCreator.getKeyForParameter(parameters);
		return index.get(key);
	}

	public void insert(Object entity) {
		Serializable[] keys = viewCreator.getKeys(entity);
		for (Serializable key : keys) {

			index.put(key, entity);
		}
	}

	// select * from EntityType e were e.derivedFrom.originalType=:type and
	// e.binding.version=:version
}
