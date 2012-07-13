/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.meta;

import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.AbstractSingleAssociationAttribute;


public class SingleMetaAttribute<J> implements SingleAttribute<J>
{
	private String code;

	private SingleAttribute<?> incomingHolderAttribute;

	private MetaAttributeService metaAttributeService;

	private SingleAttribute<J> metaDataAttribute;

	public SingleMetaAttribute(SingleAttribute<J> metaDataAttribute, AbstractSingleAssociationAttribute<J> holderAttribute,
		MetaAttributeService metaAttributeService, String code)
	{
		super();
		this.code = code;
		this.metaDataAttribute = metaDataAttribute;
		this.incomingHolderAttribute = (SingleAttribute<?>) holderAttribute.getIncomingRelation();
		if (incomingHolderAttribute == null)
		{
			throw new IllegalArgumentException("cannot create meta Attribute if holder attribute has noincoming attribute");
		}
		this.metaAttributeService = metaAttributeService;
	}

	@Override
	public Class<J> getAssociationType()
	{
		return metaDataAttribute.getAssociationType();
	}

	@Override
	public String getCode()
	{
		return code;
	}

	@Override
	public EntityType getEntityType()
	{
		return (EntityType) incomingHolderAttribute.getTargetType();
	}

	@Override
	public Class<J> getReturnType()
	{
		return metaDataAttribute.getReturnType();
	}

	@Override
	public Cardinality getTargetCardinality()
	{
		return metaDataAttribute.getTargetCardinality();
	}

	@Override
	public Type<J> getTargetType()
	{
		return metaDataAttribute.getTargetType();
	}

	@Override
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

	@Override
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

	@Override
	public boolean isRequired()
	{
		return metaDataAttribute.isRequired();
	}

	@Override
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
