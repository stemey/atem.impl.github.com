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
package org.atemsource.atem.impl.common.attribute.collection;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;
import org.atemsource.atem.api.type.Type;
import org.atemsource.atem.impl.common.attribute.AbstractAttribute;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractCollectionAttributeImpl<J, R> extends AbstractAttribute<J, R> implements
	CollectionAttribute<J, R>
{

	public static class SerializableCollection implements Serializable
	{
		public List<Serializable> elements;
	}

	@Autowired
	private EntityTypeRepository entityRepository;

	private CollectionSortType collectionSortType;

	public AbstractCollectionAttributeImpl()
	{
		super();
	}

	public void addElement(Object entity, J element)
	{
		if (getElements(entity)==null) {
			setValue(entity, createEmptyCollection());
		}
		getElements(entity).add(element);
	}

	private R createEmptyCollection() {
		try {
			return getAssociationType().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("cannot instantiate empty collection",e);
		}
	}

	public void clear(Object entity)
	{
		Collection<J> elements = getElements(entity);
		if (elements != null)
		{
			elements.clear();
		}else {
			try {
				setValue(entity,creatEmptyCollection());
			} catch (Exception e) {
				throw new TechnicalException("cannot instantiate collection",e);
			}
		}
	}

	protected abstract  R creatEmptyCollection();
	public boolean contains(Object entity, J element)
	{
		return getElements(entity).contains(element);
	}

	public CollectionSortType getCollectionSortType()
	{
		return collectionSortType;
	}

	public Iterator<J> getIterator(Object entity)
	{
		return getElements(entity).iterator();
	}

	@Override
	public Class<R> getReturnType()
	{
		return getAssociationType();
	}

	public int getSize(Object entity)
	{
		return ((Collection) getValue(entity)).size();
	}

	public Type<J> getTargetType(J value)
	{
		return entityRepository.getEntityType(value);
	}

	public R getValue(Object arg0)
	{
		return (R) getAccessor().getValue(arg0);
	}

	@Override
	public boolean isEqual(Object entity, Object other)
	{
		if (entity == null && other == null)
		{
			return true;
		}
		else if (entity == null | other == null)
		{
			return false;
		}
		else
		{
			R associatedEntitiesA = getValue(entity);
			R associatedEntitiesB = getValue(other);
			if (associatedEntitiesA == null && associatedEntitiesB == null)
			{
				return true;
			}
			else if (associatedEntitiesA == null && associatedEntitiesB != null || associatedEntitiesB == null
				&& associatedEntitiesA != null)
			{
				return false;
			}
			if (getSize(entity) != getSize(other))
			{
				return false;
			}
			else if (getCollectionSortType() == CollectionSortType.NONE)
			{
				for (J valueA : getElements(entity))
				{
					boolean foundEqualElement = false;
					for (J valueB : getElements(other))
					{
						// this follgoeing code only work for ordered collections
						if (isValueEqual(valueA, valueB))
						{
							foundEqualElement = true;
							break;
						}
					}
					if (!foundEqualElement)
					{
						return false;
					}
				}
				return true;
			}
			else
			{
				Iterator<J> iteratorA = getIterator(entity);
				Iterator<J> iteratorB = getIterator(other);
				for (int index = 0; index < getSize(entity); index++)
				{
					// this follgoeing code only work for ordered collections
					J valueA = iteratorA.next();
					J valueB = iteratorB.next();
					if (!isValueEqual(valueA, valueB))
					{
						return false;
					}
				}
				return true;
			}
		}
	}

	private boolean isValueEqual(J valueA, J valueB)
	{
		Type<J> typeA = entityRepository.getType(valueA);
		Type<J> typeB = entityRepository.getType(valueB);
		if (typeA.equals(typeB) && typeA.isEqual(valueA, valueB))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void removeElement(Object entity, J element)
	{
		getElements(entity).remove(element);
	}

	public void setCollectionSortType(CollectionSortType collectionSortType)
	{
		this.collectionSortType = collectionSortType;
	}

}
