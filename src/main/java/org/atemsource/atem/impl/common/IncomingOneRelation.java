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
package org.atemsource.atem.impl.common;


import org.atemsource.atem.api.attribute.AssociationAttribute;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.service.SingleAttributeQuery;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class IncomingOneRelation<J> implements SingleAttribute<J>, AssociationAttribute<J, J>
{

	private Cardinality targetCardinality;

	private SingleAttributeQuery attributeQuery;

	private Attribute<?, ?> attribute;

	private String code;

	public IncomingOneRelation()
	{
		super();
	}

	@Override
	public Class<J> getAssociationType()
	{
		return (Class<J>) attribute.getTargetType().getJavaType();
	}

	public Attribute<?, ?> getAttribute()
	{
		return attribute;
	}

	public SingleAttributeQuery getAttributeQuery()
	{
		return attributeQuery;
	}

	public String getCode()
	{
		return code;
	}

	@Override
	public EntityType getEntityType()
	{
		return (EntityType) attribute.getTargetType();
	}

	@Override
	public AssociationAttribute<?, ?> getIncomingRelation()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<J> getReturnType()
	{
		return attribute.getEntityType().getJavaType();
	}

	public Cardinality getTargetCardinality()
	{
		return targetCardinality;
	}

	@Override
	public EntityType<J> getTargetType()
	{
		return attribute.getEntityType();
	}

	@Override
	public Type<J> getTargetType(J value)
	{
		return attribute.getEntityType();
	}

	@Override
	public J getValue(Object entity)
	{
		if (attributeQuery == null)
		{
			throw new UnsupportedOperationException("this incomingRelation is not readable");
		}
		return (J) attributeQuery.getResult(entity);

	}

	@Override
	public boolean isComposition()
	{
		return false;
	}

	@Override
	public boolean isEqual(Object entity, Object other)
	{

		J valueA = getValue(entity);
		J valueB = getValue(other);
		return getTargetType().isEqual(valueA, valueB);
	}

	@Override
	public boolean isRequired()
	{
		return attribute.isRequired();
	}

	@Override
	public boolean isWriteable()
	{

		return false;
	}

	public void setAttribute(Attribute<?, ?> attribute)
	{
		this.attribute = attribute;
	}

	public void setAttributeQuery(SingleAttributeQuery attributeQuery)
	{
		this.attributeQuery = attributeQuery;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public void setTargetCardinality(Cardinality targetCardinality)
	{
		this.targetCardinality = targetCardinality;
	}

	@Override
	public void setValue(Object entity, J value)
	{
		throw new UnsupportedOperationException("not imlemented yet");
	}

	@Override
	public Type<J>[] getValidTargetTypes() {
		return null;
	}

}
