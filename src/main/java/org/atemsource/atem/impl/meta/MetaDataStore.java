package org.atemsource.atem.impl.meta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.type.EntityType;

public class MetaDataStore<J> {

	private IdentityService identityService;
	private SingleAttribute<J> attribute;
	private Map<Serializable, J> values = new HashMap<Serializable, J>();

	public MetaDataStore(SingleAttribute<J> attribute) {
		super();
		this.attribute = attribute;
		identityService = ((EntityType<J>) attribute.getEntityType())
				.getService(IdentityService.class);
	}

	public void insert(Object holder, J value) {
		Serializable id = identityService.getId(attribute.getEntityType(),
				holder);
		values.put(id, value);

	}

	public J find(Object holder) {
		Serializable id = identityService.getId(attribute.getEntityType(),
				holder);
		return values.get(id);
	}

}
