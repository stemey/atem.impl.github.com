package org.atemsource.atem.impl.primitive;

import java.io.Serializable;
import java.util.Date;

import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.api.type.primitive.DateType;
import org.atemsource.atem.api.type.primitive.DateType.Precision;
import org.atemsource.atem.impl.common.attribute.primitive.DateTypeImpl;
import org.atemsource.atem.spi.PrimitiveTypeRegistrar;

public class DateTypeRegistrar implements PrimitiveTypeRegistrar {

	@Override
	public PrimitiveType<?>[] getTypes() {
		return new PrimitiveType[] { new DateTypeImpl(Precision.DATETIME,
				Date.class) };
	}

}
