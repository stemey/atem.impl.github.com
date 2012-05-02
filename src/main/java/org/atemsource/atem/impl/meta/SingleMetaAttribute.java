/*******************************************************************************
 * Stefan Meyer, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.meta;


import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.SingleAssociationAttribute;


public class SingleMetaAttribute<J> implements SingleAttribute<J>
{
	private SingleAttribute<J> metaDataAttribute;

	private SingleAttribute<?> incomingHolderAttribute;

	private MetaAttributeService metaAttributeService;

	public SingleMetaAttribute(SingleAttribute<J> metaDataAttribute, SingleAssociationAttribute<?> holderAttribute,
		MetaAttributeService metaAttributeService)
	{
		super();
		this.metaDataAttribute = metaDataAttribute;
		this.incomingHolderAttribute = (SingleAttribute<?>) holderAttribute.getIncomingRelation();
		if (incomingHolderAttribute == null)
		{
			throw new IllegalArgumentException("cannot create meta Attribute if holder attribute has noincoming attribute");
		}
		this.metaAttributeService = metaAttributeService;
	}

	public Class<J> getAssociationType()
	{
		return metaDataAttribute.getAssociationType();
	}

	public String getCode()
	{
		return metaDataAttribute.getCode();
	}

	public EntityType getEntityType()
	{
		return metaDataAttribute.getEntityType();
	}

	public Class<J> getReturnType()
	{
		return metaDataAttribute.getReturnType();
	}

	public Cardinality getTargetCardinality()
	{
		return metaDataAttribute.getTargetCardinality();
	}

	public Type<J> getTargetType()
	{
		return metaDataAttribute.getTargetType();
	}

	public Type<J> getTargetType(J value)
	{
		return metaDataAttribute.getTargetType(value);
	}

	@Override
	public J getValue(Object entity)
	{
		Object intermediate = metaAttributeService.getMetaData(incomingHolderAttribute.getTargetType().getCode(), entity);
		return metaDataAttribute.getValue(intermediate);
	}

	public boolean isComposition()
	{
		return metaDataAttribute.isComposition();
	}

	@Override
	public boolean isEqual(Object entity, Object other)
	{
		Object intermediateA = incomingHolderAttribute.getValue(entity);
		Object intermediateB = incomingHolderAttribute.getValue(other);
		return metaDataAttribute.isEqual(intermediateA, intermediateB);
	}

	public boolean isRequired()
	{
		return metaDataAttribute.isRequired();
	}

	public boolean isWriteable()
	{
		return metaDataAttribute.isWriteable();
	}

	@Override
	public void setValue(Object entity, J value)
	{
		Object intermediate = metaAttributeService.getMetaData(incomingHolderAttribute.getTargetType().getCode(), entity);
		metaDataAttribute.setValue(intermediate, value);
	}

}
