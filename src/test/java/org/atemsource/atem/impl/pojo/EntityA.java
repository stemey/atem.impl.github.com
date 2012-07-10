/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.atemsource.atem.api.attribute.annotation.Association;
import org.atemsource.atem.api.attribute.annotation.MapAssociation;
import org.atemsource.atem.api.type.Entity;


@Entity
public class EntityA
{

	@Association(targetType = EntityB.class)
	private List<EntityB> list = new ArrayList<EntityB>();

	@MapAssociation(targetType = EntityB.class, keyType = String.class)
	private Map<String, EntityB> map;

	private List objectList;

	private String[] stringArray;

	public List<EntityB> getList()
	{
		return list;
	}

	public Map<String, EntityB> getMap()
	{
		return map;
	}

	public List getObjectList()
	{
		return objectList;
	}

	public String[] getStringArray()
	{
		return stringArray;
	}

	public void setList(List<EntityB> list)
	{
		this.list = list;
	}

	public void setMap(Map<String, EntityB> map)
	{
		this.map = map;
	}

	public void setObjectList(List objectList)
	{
		this.objectList = objectList;
	}

	public void setStringArray(String[] stringArray)
	{
		this.stringArray = stringArray;
	}
}
