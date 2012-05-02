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
package org.atemsource.atem.impl.common;

import org.atemsource.atem.api.service.Description;


public class DescriptionImpl implements Description
{
	private String description;

	private String imagePath;

	private String label;

	public DescriptionImpl(String label, String description)
	{
		super();
		this.description = description;
		this.label = label;
	}

	public DescriptionImpl(String label, String description, String imagePath)
	{
		super();
		this.description = description;
		this.label = label;
		this.imagePath = imagePath;
	}

	public String getDescription()
	{
		return description;
	}

	public String getImagePath()
	{
		return imagePath;
	}

	public String getLabel()
	{
		return label;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}
}
