package org.atemsource.atem.impl.pojo.persistence;

import java.io.Serializable;

public interface ViewCreator {

	public Serializable[] getKeys(Object entity);
	public Serializable getKeyForParameter(Object[] parameter);
	public Class[] getParameterTypes();
}
