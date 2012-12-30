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
package org.atemsource.atem.impl.pojo.attribute;

import java.lang.annotation.Annotation;

import org.atemsource.atem.api.attribute.Accessor;
import org.atemsource.atem.api.attribute.annotation.Association;

public class PropertyDescriptor {

	private String propertyName;

	private Accessor accessor;

	private Class propertyType;
	private boolean writable;

	private boolean derived;

	public PropertyDescriptor() {

	}

	public String getPropertyName() {
		return propertyName;
	}

	public Accessor getAccessor() {
		return accessor;
	}

	public Class getPropertyType() {
		return propertyType;
	}

	public boolean isWritable() {
		return writable;
	}

	public PropertyDescriptor(String propertyName, Accessor accessor,
			Class type, boolean writable,boolean derived) {
		super();
		this.propertyName = propertyName;
		this.accessor = accessor;
		this.propertyType = type;
		this.writable = writable;
		this.derived=derived;
	}

	public boolean isDerived() {
		return derived;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return accessor.getAnnotation(annotationClass);
	}

}
