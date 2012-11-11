/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.common;

import javax.inject.Inject;
import org.atemsource.atem.api.BeanLocator;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.MapAttribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.EntityTypeBuilder;
import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.MapAttributeImpl;
import org.atemsource.atem.impl.common.attribute.PrimitiveAttributeImpl;
import org.atemsource.atem.impl.common.attribute.SingleAssociationAttribute;
import org.atemsource.atem.impl.common.attribute.SingleAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.AbstractCollectionAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.ListAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.SetAttributeImpl;
import org.atemsource.atem.impl.dynamic.attribute.DynamicAccessor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Scope("prototype")
@Component
public class AbstractEntityTypeBuilder implements EntityTypeBuilder
{

	@Inject
	protected BeanLocator beanLocator;

	private EntityTypeBuilderCallback callback;

	private AbstractEntityType<?> entityType;

	@Inject
	private EntityTypeRepository entityTypeRepository;

	protected void addAttribute(Attribute<?, ?> attribute)
	{
		entityType.addAttribute(attribute);
	}

	@Override
	public <K, V, Map> MapAttribute<K, V, Map> addMapAssociationAttribute(String code, Type<K> keyType,
		Type<V> valueType, boolean sorted)
	{
		return addMapAssociationAttribute(code, keyType, valueType, sorted, null);
	}

	@Override
	public <K, V, Map> MapAttribute<K, V, Map> addMapAssociationAttribute(String code, Type<K> keyType,
		Type<V> valueType, boolean sorted, Type[] validTypes)
	{
		MapAttributeImpl attribute = beanLocator.getInstance(MapAttributeImpl.class);
		attribute.setAccessor(new DynamicAccessor(code));
		// attribute.setValidTypesSet(valueType.getSelfAndAllSubEntityTypes());
		attribute.setKeyType(keyType);
		attribute.setCode(code);
		attribute.setEntityType(entityType);
		attribute.setTargetType(valueType);
		attribute.setValidTargetTypes(validTypes);
		attribute.setSorted(sorted);
		attribute.setWriteable(true);
		addAttribute(attribute);
		return attribute;
	}

	@Override
	public CollectionAttribute addMultiAssociationAttribute(String code, Type targetType,
		CollectionSortType collectionSortType)
	{
		return addMultiAssociationAttribute(code, targetType, null, collectionSortType);
	}

	@Override
	public CollectionAttribute addMultiAssociationAttribute(String code, Type targetType, Type[] validTypes,
		CollectionSortType collectionSortType)
	{
		AbstractCollectionAttributeImpl attribute;
		switch (collectionSortType)
		{
			case ORDERABLE:
				attribute = beanLocator.getInstance(ListAttributeImpl.class);
			break;
			case SORTED:
				throw new UnsupportedOperationException("sorted collectionsare not spported");
			default:
				attribute = beanLocator.getInstance(SetAttributeImpl.class);
			break;
		}
		attribute.setCollectionSortType(collectionSortType);
		attribute.setAccessor(new DynamicAccessor(code));
		attribute.setWriteable(true);
		attribute.setCode(code);
		attribute.setAccessor(new DynamicAccessor(code));
		attribute.setEntityType(entityType);
		attribute.setTargetType(targetType);
		attribute.setValidTargetTypes(validTypes);
		addAttribute(attribute);
		return attribute;
	}

	@Override
	public <J> SingleAttribute<J> addPrimitiveAttribute(String code, PrimitiveType<J> type)
	{
		SingleAttributeImpl<J> attribute;
		attribute = new PrimitiveAttributeImpl<J>();
		attribute.setAccessor(new DynamicAccessor(code));
		attribute.setWriteable(true);
		attribute.setTargetType(type);
		attribute.setCode(code);
		addAttribute(attribute);
		attribute.setEntityType(entityType);
		return attribute;
	}

	@Override
	public <J> SingleAttribute<J> addSingleAssociationAttribute(String code, EntityType<J> targetType)
	{
		return addSingleAssociationAttribute(code, targetType, null);
	}

	public <J> SingleAttribute<J> addSingleAssociationAttribute(String code, EntityType<J> targetType, Type[] validTypes)
	{
		SingleAssociationAttribute<J> attribute = beanLocator.getInstance(SingleAssociationAttribute.class);
		attribute.setAccessor(new DynamicAccessor(code));
		attribute.setCode(code);
		attribute.setTargetType(targetType);
		attribute.setEntityType(entityType);
		attribute.setValidTargetTypes(validTypes);
		attribute.setWriteable(true);
		addAttribute(attribute);
		return attribute;
	}

	@Override
	public <J> SingleAttribute<J> addSingleAttribute(String code, Class<J> javaType)
	{
		return addSingleAttribute(code, entityTypeRepository.getType(javaType));
	}

	@Override
	public <J> SingleAttribute<J> addSingleAttribute(String code, Class<J> javaType, Class[] validClasses)
	{
		Type[] validTypes = new Type[validClasses.length];
		for (int i = 0; i < validClasses.length; i++)
		{
			validTypes[i] = entityTypeRepository.getType(validClasses[i]);
		}
		return addSingleAttribute(code, entityTypeRepository.getType(javaType), validTypes);
	}

	@Override
	public <J> SingleAttribute<J> addSingleAttribute(String code, Type<J> type)
	{
		return addSingleAttribute(code, type, null);
	}

	@Override
	public <J> SingleAttribute<J> addSingleAttribute(String code, Type<J> type, Type[] validTypes)
	{
		if (type instanceof EntityType<?>)
		{
			return addSingleAssociationAttribute(code, (EntityType<J>) type, validTypes);
		}
		else
		{
			return addPrimitiveAttribute(code, (PrimitiveType<J>) type);
		}
	}

	@Override
	public EntityType<?> createEntityType()
	{
		callback.onFinished(entityType);
		return entityType;
	}

	public AbstractEntityType<?> getEntityType()
	{
		return entityType;
	}

	@Override
	public void mixin(EntityType<?> mixinType)
	{
		entityType.addMixin(mixinType);
	}

	@Override
	public void setAbstract(boolean abstractType)
	{
		entityType.setAbstractType(abstractType);
	}

	@Override
	public EntityTypeBuilder setEntityClass(Class<?> entityClass)
	{
		entityType.setEntityClass(entityClass);
		return this;
	}

	public void setEntityType(AbstractEntityType<?> entityType)
	{
		this.entityType = entityType;
	}

	public void setRepositoryCallback(EntityTypeBuilderCallback callback)
	{
		this.callback = callback;
	}

	@Override
	public void superType(EntityType<?> superType)
	{
		entityType.setSuperEntityType(superType);
		// TODO this should be done by repository. It might span more than one
		// repository.
		if (superType instanceof AbstractEntityType)
		{
			((AbstractEntityType) superType).addSubEntityType(entityType);
		}
	}

	public interface EntityTypeBuilderCallback
	{

		void onFinished(AbstractEntityType<?> entityType);

	}
}
