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

import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.CollectionSortType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class CollectionAttributeImpl<J> extends AbstractCollectionAttributeImpl<J, Collection> implements
	CollectionAttribute<J, Collection>
{

	@Override
	public Class<Collection> getAssociationType()
	{
		return Collection.class;
	}

	@Override
	public CollectionSortType getCollectionSortType()
	{
		return CollectionSortType.NONE;
	}

	@Override
	public Collection<J> getElements(Object entity)
	{
		return getValue(entity);
	}

	@Override
	public Collection getEmptyCollection(Object entity)
	{
		return new ArrayList();
	}

}
