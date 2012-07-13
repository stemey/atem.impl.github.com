package org.atemsource.atem.impl.json;

import org.atemsource.atem.api.type.EntityType;


public interface TypeCodeConverter
{

	public String fromExternalCode(EntityType<?> entityType, String typeCode);

	public String toExternalCode(EntityType<?> entityType);

}
