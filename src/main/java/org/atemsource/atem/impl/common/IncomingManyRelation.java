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


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.atemsource.atem.api.attribute.relation.SetAssociationAttribute;
import org.atemsource.atem.api.service.AttributeQuery;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.common.attribute.collection.AbstractCollectionAttributeImpl;
import org.atemsource.atem.impl.common.attribute.collection.SetAttributeImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class IncomingManyRelation<J> extends AbstractCollectionAttributeImpl<J, Set> implements SetAssociationAttribute<J>
{

	private AttributeQuery attributeQuery;

	@Override
	public void addElement(Object entity, J element)
	{
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public void clear(Object entity)
	{
		throw new UnsupportedOperationException("not implemented");
	}

	public AttributeQuery getAttributeQuery()
	{
		return attributeQuery;
	}

	@Override
	public EntityType<J> getTargetType(J value)
	{
		return (EntityType<J>) super.getTargetType(value);
	}

	@Override
	public Set<J> getValue(Object entity)
	{
		if (attributeQuery == null)
		{
			throw new UnsupportedOperationException("this incomingRelation is not readable");
		}
		HashSet<J> hashSet = new HashSet<J>();
		hashSet.addAll((Collection<? extends J>) attributeQuery.getResult(entity));
		return hashSet;
	}

	@Override
	public boolean isWriteable()
	{
		return false;
	}

	@Override
	public void removeElement(Object entity, J element)
	{
		throw new UnsupportedOperationException("not implemented");
	}

	public void setAttributeQuery(AttributeQuery attributeQuery)
	{
		this.attributeQuery = attributeQuery;
	}

	@Override
	public Class<Set> getAssociationType()
	{
		return Set.class;
	}

	@Override
	public Collection<J> getElements(Object entity)
	{
		Set<J> value = getValue(entity);
		return value;
	}

	@Override
	public Set getEmptyCollection(Object entity)
	{
		return new HashSet();
	}


}
