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


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.atemsource.atem.api.attribute.CollectionSortType;
import org.atemsource.atem.api.attribute.relation.ListAssociationAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class ListAttributeImpl<J> extends AbstractCollectionAttributeImpl<J, List> implements
	ListAssociationAttribute<J>
{
	@Override
	public void addElement(Object entity, int index, J value)
	{
		List list = getValue(entity);
		list.add(index, value);
	}

	@Override
	public Class<List> getAssociationType()
	{
		return List.class;
	}

	@Override
	public CollectionSortType getCollectionSortType()
	{
		return CollectionSortType.ORDERABLE;
	}

	@Override
	public Object getElement(Object entity, int index)
	{
		return getValue(entity).get(index);
	}

	@Override
	public Collection<J> getElements(Object entity)
	{
		final List<J> elements = getValue(entity);
		return elements;
	}



	@Override
	public int getIndex(Object entity, J value)
	{
		return getValue(entity).indexOf(value);
	}

	@Override
	public void moveElement(Object entity, int fromIndex, int toIndex)
	{
		List value = getValue(entity);
		if (value == null || value.size() <= fromIndex || value.size() <= toIndex)
		{
			throw new IllegalArgumentException("list is too small");
		}
		Object element = value.remove(fromIndex);
		value.add(toIndex, element);

	}

	@Override
	public Object removeElement(Object entity, int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List creatEmptyCollection() {
		return new LinkedList<J>();
	}

}
