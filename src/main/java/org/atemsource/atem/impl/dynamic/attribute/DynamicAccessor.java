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
package org.atemsource.atem.impl.dynamic.attribute;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

import org.atemsource.atem.api.attribute.Accessor;
import org.atemsource.atem.impl.dynamic.DynamicEntity;


public class DynamicAccessor implements Accessor
{

	private String code;

	public DynamicAccessor(String code)
	{
		super();
		this.code = code;
	}

	

	public Method getReadMethod()
	{
		return null;
	}

	@Override
	public Object getValue(Object entity)
	{
		return ((DynamicEntity) entity).get(code);
	}

	@Override
	public boolean isReadable()
	{
		return true;
	}

	@Override
	public boolean isWritable()
	{
		return true;
	}

	@Override
	public void setValue(Object entity, Object value)
	{
		((DynamicEntity) entity).put(code, value);
	}

}
