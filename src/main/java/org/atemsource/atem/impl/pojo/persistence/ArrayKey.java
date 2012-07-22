package org.atemsource.atem.impl.pojo.persistence;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayKey implements Serializable {

	private List<Serializable> keys;
	private int hasCode;

	public ArrayKey(Serializable[] keys) {
		super();
		this.keys = Collections.unmodifiableList(Arrays.asList(keys));
		this.hasCode = this.keys.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ArrayKey) {
			return keys.equals(((ArrayKey) obj).keys);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return hasCode;
	}

}
