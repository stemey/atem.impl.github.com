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
package org.atemsource.atem.impl.common.attribute;

import org.atemsource.atem.api.attribute.relation.SingleAttribute;


public abstract class SingleAttributeImpl<J> extends AbstractAttribute<J, J> implements SingleAttribute<J>
{

	public SingleAttributeImpl()
	{
		super();
	}

	@Override
	public Class<J> getAssociationType()
	{
		return getTargetType().getJavaType();
	}

	@Override
	public Class<J> getReturnType()
	{
		return getTargetType().getJavaType();
	}

	@Override
	public J getValue(final Object entity)
	{
		return (J) getAccessor().getValue(entity);
	}

	@Override
	public void setValue(final Object entity, final J value)
	{
		getAccessor().setValue(entity, value);

	}

}
