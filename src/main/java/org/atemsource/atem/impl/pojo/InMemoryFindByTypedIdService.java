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

import org.atemsource.atem.api.service.FindByTypedIdService;
import org.atemsource.atem.api.type.TypedId;


public class InMemoryFindByTypedIdService implements FindByTypedIdService
{
	private InMemoryPojoRepository inMemoryPojoRepository;

	@Override
	public Object findByTypedId(TypedId typedId)
	{
		return inMemoryPojoRepository.findByTypedId(typedId);
	}

}
