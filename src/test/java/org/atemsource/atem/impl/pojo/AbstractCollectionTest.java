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
package org.atemsource.atem.impl.pojo;


import java.util.ArrayList;

import javax.annotation.Resource;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.CollectionAttribute;
import org.atemsource.atem.api.attribute.relation.ListAssociationAttribute;
import org.atemsource.atem.api.type.EntityType;

import junit.framework.Assert;
import junit.framework.TestCase;


public abstract class AbstractCollectionTest extends TestCase
{

	@Resource
	protected EntityTypeRepository entityTypeRepository;

	public AbstractCollectionTest()
	{
		super();
	}

	public AbstractCollectionTest(String name)
	{
		super(name);
	}

	protected EntityA createA()
	{
		EntityA a = new EntityA();
		a.setList(new ArrayList<EntityB>());
		final EntityB b = new EntityB();
		b.setInteger(3);
		a.getList().add(b);
		return a;
	}

	protected CollectionAttribute getCollectionAssociationAttribute()
	{
		EntityType entityType = entityTypeRepository.getEntityType(EntityA.class);
		final ListAssociationAttribute attribute = (ListAssociationAttribute) entityType.getAttribute("list");
		Assert.assertNotNull(attribute);
		return attribute;
	}

}
