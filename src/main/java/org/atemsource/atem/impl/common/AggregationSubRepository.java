package org.atemsource.atem.impl.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.atemsource.atem.spi.EntityTypeSubrepository;

public class AggregationSubRepository<J> implements EntityTypeSubrepository<J> {


	List<EntityTypeSubrepository> entityTypeSubrepositories;

	@Override
	public Collection<EntityType<J>> getEntityTypes() {
		List<EntityType<J>> entityTypes = new ArrayList<EntityType<J>>();
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			entityTypes.addAll(entityTypeSubrepository.getEntityTypes());
		}
		return entityTypes;
	}

	@Override
	public void addIncomingAssociation(EntityType<J> entityType,
			Attribute<?, ?> incomingRelation) {
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			if (entityTypeSubrepository.contains(entityType)) {
				entityTypeSubrepository.addIncomingAssociation(entityType,
						incomingRelation);
			}
		}
	}

	@Override
	public void addMetaAttribute(EntityType<J> entityType,
			Attribute<?, ?> metaAttribute) {
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			if (entityTypeSubrepository.contains(entityType)) {
				entityTypeSubrepository.addMetaAttribute(entityType,
						metaAttribute);
			}
		}
	}

	@Override
	public EntityType getEntityType(Class clazz) {
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			EntityType entityType = entityTypeSubrepository
					.getEntityType(clazz);
			if (entityType != null) {
				return entityType;
			}
		}
		return null;
	}

	@Override
	public EntityType getEntityType(Object entity) {
		for (EntityTypeSubrepository subrepository : entityTypeSubrepositories) {
			EntityType entityType = subrepository.getEntityType(entity);
			if (entityType != null) {
				return entityType;
			}
		}
		return null;
	}

	@Override
	public EntityType getEntityType(String typeCode) {
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			EntityType entityType = entityTypeSubrepository
					.getEntityType(typeCode);
			if (entityType != null) {
				return entityType;
			}
		}
		return null;
	}

	@Override
	public EntityType<J> getEntityTypeReference(Class<J> clazz) {
		EntityType entityType = null;
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			entityType = entityTypeSubrepository.getEntityTypeReference(clazz);
			if (entityType != null) {
				break;
			}

		}
		if (entityType == null) {
			return null;
		}
		return entityType;
	}

	@Override
	public EntityType getEntityTypeReference(String typeCode) {
		EntityType entityType = null;
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			entityType = entityTypeSubrepository
					.getEntityTypeReference(typeCode);
			if (entityType != null) {
				break;
			}
		}
		if (entityType == null) {
			return null;
		}
		return entityType;

	}

	@Override
	public boolean hasEntityTypeReference(Class entityClass) {
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			if (entityTypeSubrepository.hasEntityTypeReference(entityClass)) {
				return true;
			}
		}
		return false;
	}

	public void setRepositories(
			List<EntityTypeSubrepository> entityTypeSubrepositories) {
		this.entityTypeSubrepositories = entityTypeSubrepositories;
	}

	@Override
	public void afterFirstInitialization(EntityTypeRepository repository) {
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			entityTypeSubrepository.afterFirstInitialization(repository);
		}
	}

	@Override
	public void afterInitialization() {
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			entityTypeSubrepository.afterInitialization();
		}
	}

	@Override
	public boolean contains(EntityType<J> entityType) {
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			if (entityTypeSubrepository.contains(entityType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void initialize(EntityTypeCreationContext ctx) {
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			entityTypeSubrepository.initialize(ctx);
		}
	}

	@Override
	public void initializeIncomingAssociations() {
		for (EntityTypeSubrepository entityTypeSubrepository : entityTypeSubrepositories) {
			entityTypeSubrepository.initializeIncomingAssociations();
		}
	}

}
