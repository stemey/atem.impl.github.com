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


import java.io.Serializable;
import java.lang.annotation.Annotation;

import org.atemsource.atem.api.EntityTypeCreationContext;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.JavaMetaData;
import org.atemsource.atem.api.meta.EntityTypePostProcessor;
import org.atemsource.atem.api.meta.ViewData;
import org.atemsource.atem.api.service.FindByTypedIdService;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.service.PersistenceService;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.impl.meta.MetaAttributeService;
import org.springframework.beans.factory.annotation.Autowired;


public class ViewDataEntityTypePostProcessor implements EntityTypePostProcessor
{
	private EntityType<?> viewEntityType;

	@Autowired
	private MetaAttributeService metaAttributeService;

	@Override
	public void postProcessEntityType(EntityTypeCreationContext context, EntityType<?> entityType)
	{
		if (viewEntityType == null)
		{
			viewEntityType = context.getEntityTypeReference(ViewData.class);
			if (viewEntityType == null)
			{
				return;
			}
		}
		for (Attribute attribute : entityType.getAttributes())
		{
			if (attribute instanceof JavaMetaData)
			{
				for (Annotation annotation : ((JavaMetaData) attribute).getAnnotations())
				{
					View view = annotation.getClass().getAnnotation(View.class);
					if (view == null)
					{
						view = annotation.getClass().getInterfaces()[0].getAnnotation(View.class);
					}
					if (view != null)
					{
						ViewData viewData = new ViewData();
						viewData.setName(view.name());
						Serializable id = viewEntityType.getService(IdentityService.class).getId(viewEntityType, viewData);
						ViewData existingViewData =
							(ViewData) viewEntityType.getService(FindByTypedIdService.class).findByTypedId(
								new SerializableTypeId(id, viewEntityType.getJavaType()));
						if (existingViewData == null)
						{
							existingViewData = viewData;
							viewEntityType.getService(PersistenceService.class).insert(existingViewData);
						}
						existingViewData.getAttributes().add(attribute);
					}
				}
			}
		}
	}
}
