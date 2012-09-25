package org.atemsource.atem.impl.meta;

public interface MetaDataService {
	public Object getMetaData(Object entity, MetaAttribute attribute);
	public void setMetaData(Object entity, Object value,MetaAttribute attribute);
}
