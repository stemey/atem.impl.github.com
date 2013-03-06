package org.atemsource.atem.impl.common.attribute;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class SingleAttributeImpl<J> extends SingleAbstractAttribute<J>
{

	@Inject
	private EntityTypeRepository entityTypeRepository;

	@Override
	public Type<J> getTargetType(J value)
	{
		return entityTypeRepository.getType(value);
	}

}
