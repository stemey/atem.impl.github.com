/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.common.attribute.collection;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class ArrayAttributeImpl<J> extends AbstractCollectionAttributeImpl<J, J[]> implements
	CollectionAttribute<J, J[]>
{

	@Override
	public void clear(Object entity)
	{
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Class<J[]> getAssociationType()
	{
		return (Class<J[]>) Array.newInstance(getTargetType().getJavaType(), 0).getClass();
	}

	@Override
	public CollectionSortType getCollectionSortType()
	{
		return CollectionSortType.ORDERABLE;
	}

	@Override
	public Collection<J> getElements(Object entity)
	{
		final J[] array = getValue(entity);
		if (array == null)
		{
			return null;
		}
		else
		{
			return Arrays.asList(array);
		}
	}

	@Override
	public J[] getEmptyCollection(Object entity)
	{
		return (J[]) Array.newInstance(getTargetType().getJavaType(), 0);
	}

	@Override
	public int getSize(Object entity)
	{
		return ((Object[]) getAccessor().getValue(entity)).length;
	}

}
