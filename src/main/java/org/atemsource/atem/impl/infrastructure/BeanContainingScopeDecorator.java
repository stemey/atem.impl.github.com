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
package org.atemsource.atem.impl.infrastructure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;


public class BeanContainingScopeDecorator implements Scope
{
	private static class NullObjctFactory implements ObjectFactory
	{
		@Override
		public Object getObject() throws BeansException
		{
			throw new IllegalStateException("we dont want to create a bean but just test if it exists");
		}
	}

	private final Scope wrapped;

	private final ObjectFactory nullFactory = new NullObjctFactory();

	public BeanContainingScopeDecorator(Scope wrapped)
	{
		this.wrapped = wrapped;
	}

	public boolean containsBean(String name)
	{
		try
		{
			wrapped.get(name, nullFactory);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	@Override
	public Object get(String name, ObjectFactory factory)
	{
		return wrapped.get(name, factory);
	}

	@Override
	public String getConversationId()
	{
		return wrapped.getConversationId();
	}

	@Override
	public void registerDestructionCallback(String s, Runnable runnable)
	{
		wrapped.registerDestructionCallback(s, runnable);
	}

	@Override
	public Object remove(String name)
	{
		return wrapped.remove(name);
	}

	@Override
	public Object resolveContextualObject(String key)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
