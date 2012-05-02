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
package org.atemsource.atem.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class EqualityUtils
{

	public static boolean isEqual(Serializable o1, Serializable o2)
	{
		try
		{
			return serialize(o1).equals(serialize(o2));
		}
		catch (IOException e)
		{
			throw new RuntimeException("cannot serialize object", e);
		}
	}

	public static String serialize(Object o) throws IOException
	{
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(byteArrayOutputStream);
		stream.writeObject(o);
		return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
	}

}
