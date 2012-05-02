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


import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.type.EntityType;
import org.springframework.stereotype.Service;


@Service
public class EntityTypeUtils
{

	public static String[] convertToArray(final Set<String> selectableTypeCodes)
	{
		return selectableTypeCodes.toArray(new String[selectableTypeCodes.size()]);
	}

	public static <T extends Collection<String>> T convertToCode(final Collection<EntityType> entityTypes,
		final T targetCollection)
	{
		for (EntityType entityType : entityTypes)
		{
			targetCollection.add(entityType.getCode());
		}
		return targetCollection;
	}

	public static <R extends Collection<EntityType<?>>> String[] convertToTypeCodeArray(final R entityTypes)
	{
		String[] typeCodes = new String[entityTypes.size()];
		int index = 0;
		for (EntityType entityType : entityTypes)
		{
			typeCodes[index] = entityType.getCode();
			index++;
		}
		return typeCodes;
	}

	@Resource
	private EntityTypeRepository entityTypeRepository;

	public <T extends Collection<EntityType>> T convertToCodes(final Set<String> typeCodes, final T hashSet)
	{
		for (String typeCode : typeCodes)
		{
			hashSet.add(entityTypeRepository.getEntityType(typeCode));
		}
		return hashSet;
	}

}
