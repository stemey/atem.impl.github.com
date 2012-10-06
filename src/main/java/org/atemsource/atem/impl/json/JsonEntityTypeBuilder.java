/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.json;

import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.MapAttribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.PrimitiveType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.AbstractEntityTypeBuilder;
import org.atemsource.atem.impl.common.attribute.SingleAttributeImpl;
import org.atemsource.atem.impl.common.attribute.primitive.BooleanTypeImpl;
import org.atemsource.atem.impl.common.attribute.primitive.DoubleType;
import org.atemsource.atem.impl.common.attribute.primitive.IntegerType;
import org.atemsource.atem.impl.common.attribute.primitive.LongType;
import org.atemsource.atem.impl.common.attribute.primitive.SimpleEnumType;
import org.atemsource.atem.impl.common.attribute.primitive.SimpleTextType;
import org.atemsource.atem.impl.json.attribute.ArrayNodeAttribute;
import org.atemsource.atem.impl.json.attribute.BooleanAttribute;
import org.atemsource.atem.impl.json.attribute.DoubleAttribute;
import org.atemsource.atem.impl.json.attribute.IntegerAttribute;
import org.atemsource.atem.impl.json.attribute.LongAttribute;
import org.atemsource.atem.impl.json.attribute.MapNodeAttribute;
import org.atemsource.atem.impl.json.attribute.ObjectNodeAttribute;
import org.atemsource.atem.impl.json.attribute.StringAttribute;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class JsonEntityTypeBuilder extends AbstractEntityTypeBuilder
{

	private ObjectMapper objectMapper;

	@Override
	public <K, V, ObjectNode> MapAttribute<K, V, ObjectNode> addMapAssociationAttribute(String name, Type<K> keyType,
		Type<V> valueType,Type[] validTypes)
	{
		if (keyType.getJavaType() != String.class)
		{
			throw new IllegalArgumentException("for json only keys of type string are possibkle");
		}
		MapNodeAttribute mapAttribute = beanLocator.getInstance(MapNodeAttribute.class);
		mapAttribute.setCode(name);
		mapAttribute.setEntityType(getEntityType());
		mapAttribute.setTargetType(valueType);
		mapAttribute.setWriteable(true);
		mapAttribute.setObjectMapper(objectMapper);
		addAttribute(mapAttribute);
		mapAttribute.setValidTargetTypes(validTypes);
	return mapAttribute;
	}

	@Override
	public CollectionAttribute addMultiAssociationAttribute(String code, Type targetType,Type[] validTypes,
		CollectionSortType collectionSortType)
	{
		ArrayNodeAttribute attribute = beanLocator.getInstance(ArrayNodeAttribute.class);
		attribute.setCode(code);
		attribute.setEntityType(getEntityType());
		attribute.setTargetType(targetType);
		addAttribute(attribute);
		attribute.setWriteable(true);
		attribute.setObjectMapper(objectMapper);
		attribute.setValidTargetTypes(validTypes);

		return attribute;
	}

	@Override
	public <J> SingleAttribute<J> addPrimitiveAttribute(String code, PrimitiveType<J> type)
	{
		SingleAttributeImpl attribute;
		if (type.getJavaType().isAssignableFrom(Long.class) || type.getJavaType().isAssignableFrom(long.class))
		{
			attribute = beanLocator.getInstance(LongAttribute.class);
			attribute.setTargetType(new LongType(true));
		}
		else if (type.getJavaType().isAssignableFrom(Integer.class) || type.getJavaType().isAssignableFrom(int.class))
		{
			attribute = beanLocator.getInstance(IntegerAttribute.class);
			attribute.setTargetType(new IntegerType());
		}
		else if (type.getJavaType().isAssignableFrom(String.class))
		{
			attribute = beanLocator.getInstance(StringAttribute.class);
			attribute.setTargetType(new SimpleTextType());
		}
		else if (type.getJavaType().isAssignableFrom(Double.class) || type.getJavaType().isAssignableFrom(double.class))
		{
			attribute = beanLocator.getInstance(DoubleAttribute.class);
			attribute.setTargetType(new DoubleType());
		}
		else if (type.getJavaType().isAssignableFrom(Boolean.class) || type.getJavaType().isAssignableFrom(boolean.class))
		{
			attribute = beanLocator.getInstance(BooleanAttribute.class);
			attribute.setTargetType(new BooleanTypeImpl());
		}
		else
		{
			throw new UnsupportedOperationException("type " + type.getJavaType().getName() + " is not implemented yet");
		}
		attribute.setCode(code);
		addAttribute(attribute);
		attribute.setEntityType(getEntityType());
		attribute.setWriteable(true);
		return attribute;
	}

	@Override
	public SingleAttribute addSingleAssociationAttribute(String code, EntityType targetType,Type[] validTypes)
	{
		ObjectNodeAttribute attribute = beanLocator.getInstance(ObjectNodeAttribute.class);
		attribute.setCode(code);
		attribute.setEntityType(getEntityType());
		attribute.setTargetType(targetType);
		addAttribute(attribute);
		attribute.setWriteable(true);
		attribute.setValidTargetTypes(validTypes);

		return attribute;
	}

	@Override
	public <J> SingleAttribute<J> addSingleAttribute(String code, Type<J> type,Type[] validTypes)
	{
		if (type == null || type instanceof EntityType<?>)
		{
			return addSingleAssociationAttribute(code, (EntityType<J>) type,validTypes);
		}
		else
		{
			return addPrimitiveAttribute(code, (PrimitiveType<J>) type);
		}
	}

	public ObjectMapper getObjectMapper()
	{
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

}
