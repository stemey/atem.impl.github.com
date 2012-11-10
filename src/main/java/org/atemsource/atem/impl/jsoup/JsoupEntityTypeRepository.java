package org.atemsource.atem.impl.jsoup;

import javax.inject.Inject;

import org.atemsource.atem.api.BeanLocator;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.EntityTypeBuilder;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.AbstractMetaDataRepository;
import org.atemsource.atem.impl.json.JsonEntityTypeImpl;
import org.atemsource.atem.impl.jsoup.attribute.AttributesAttribute;
import org.atemsource.atem.impl.jsoup.attribute.ChildrenAttribute;
import org.atemsource.atem.impl.jsoup.attribute.NodesAttribute;
import org.atemsource.atem.spi.DynamicEntityTypeSubrepository;
import org.atemsource.atem.spi.EntityTypeCreationContext;
import org.jsoup.nodes.Node;

public class JsoupEntityTypeRepository extends
		AbstractMetaDataRepository<Node> implements
		DynamicEntityTypeSubrepository<Node> {
	
	@Inject
	private BeanLocator beanLocator;

	private EntityType<Node> nodeType;

	@Override
	public void afterFirstInitialization(
			EntityTypeRepository entityTypeRepositoryImpl) {
	}

	@Override
	public void afterInitialization() {
	}

	@Override
	public EntityTypeBuilder createBuilder(String code) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public JsonEntityTypeImpl createEntityType(String code) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public EntityType<Node> getEntityType(Object entity) {
		if (entity instanceof Node) {
			return nodeType;
		}

		return null;
	}



	@Override
	public void initialize(EntityTypeCreationContext entityTypeCreationContext) {
		this.entityTypeCreationContext = entityTypeCreationContext;
		
		JsoupEntityType nodeType = beanLocator.getInstance(JsoupEntityType.class);
		
		ChildrenAttribute children = new ChildrenAttribute(nodeType);
		Type<String> textType = (Type<String>) entityTypeCreationContext.getTypeReference(String.class);
		AttributesAttribute attributes = new AttributesAttribute(textType,nodeType);
		this.nodeType=nodeType;
		classToEntityTypes.put(Node.class, nodeType);
		nameToEntityTypes.put(Node.class.getName(),nodeType);
		entityTypes.add(nodeType);
	}



}
