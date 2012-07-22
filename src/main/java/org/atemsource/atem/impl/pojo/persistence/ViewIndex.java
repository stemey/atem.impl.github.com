package org.atemsource.atem.impl.pojo.persistence;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ViewIndex {

	private ViewCreator viewCreator;

	private Map<Serializable, Set<Object>> index;

	public ViewIndex(ViewCreator viewCreator) {
		this.viewCreator=viewCreator;
	}

	public void create(Set<Object> entities) {
		for (Object entity : entities) {
			insert(entity);
		}
	}

	public void insert(Object entity) {
		Serializable[] keys = viewCreator.getKeys(entity);
		for (Serializable key : keys) {
			Set<Object> set = index.get(key);
			if (set == null) {
				set = new HashSet<Object>();
			}
			set.add(entity);
		}
	}

	public Set find(Object[] parameters) {
		Serializable key = viewCreator.getKeyForParameter(parameters);
		return index.get(key);
	}

	

	// select * from EntityType e were e.derivedFrom.originalType=:type and
	// e.binding.version=:version
}
