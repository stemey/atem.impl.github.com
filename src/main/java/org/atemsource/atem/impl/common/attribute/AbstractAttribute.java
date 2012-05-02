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


import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

import org.atemsource.atem.api.TechnicalException;
import org.atemsource.atem.api.attribute.Accessor;
import org.atemsource.atem.api.attribute.AssociationAttribute;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.JavaMetaData;
import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.api.type.Type;


public abstract class AbstractAttribute<J, R> implements org.atemsource.atem.api.attribute.Attribute<J, R>,
	JavaMetaData
{

	private String code;

	private Accessor accessor;

	private AssociationAttribute<?, ?> incomingRelation;

	private boolean composition;

	private Cardinality targetCardinality;

	private EntityType entityType;

	private Type<J> targetType;

	private boolean writeable;

	private boolean required;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractAttribute other = (AbstractAttribute) obj;
		if (code == null)
		{
			if (other.code != null)
				return false;
		}
		else if (!code.equals(other.code))
			return false;
		if (entityType == null)
		{
			if (other.entityType != null)
				return false;
		}
		else if (!entityType.equals(other.entityType))
			return false;
		return true;
	}

	public Accessor getAccessor()
	{
		return accessor;
	}

	@Override
	public <A extends Annotation> A getAnnotation(final Class<A> annotationClass)
	{
		if (accessor != null)
		{
			return accessor.getAnnotation(annotationClass);
		}
		else
		{
			return null;
		}
	}

	@Override
	public Annotation getAnnotationAnnotatedBy(Class<? extends Annotation> annotationClass)
	{
		if (accessor != null)
		{
			return accessor.getAnnotationAnnotatedBy(annotationClass);
		}
		else
		{
			return null;
		}
	}

	@Override
	public Collection<? extends Annotation> getAnnotations()
	{
		if (accessor != null)
		{
			return accessor.getAnnotations();
		}
		else
		{
			return Collections.EMPTY_SET;
		}
	}

	@Override
	public String getCode()
	{
		return code;
	}

	@Override
	public EntityType<J> getEntityType()
	{
		return entityType;
	}

	public AssociationAttribute<?, ?> getIncomingRelation()
	{
		return incomingRelation;
	}

	public Cardinality getTargetCardinality()
	{
		return targetCardinality;
	}

	@Override
	public Type<J> getTargetType()
	{
		return targetType;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((entityType == null) ? 0 : entityType.hashCode());
		return result;
	}

	@Override
	public boolean isComposition()
	{
		return composition;
	}

	public boolean isEditable()
	{
		return isWriteable();
	}

	@Override
	public boolean isEqual(Object entity, Object other)
	{
		Object value = getValue(entity);
		Object otherValue = getValue(other);
		if (value == null)
		{
			if (otherValue != null)
			{
				return false;
			}
		}
		else if (otherValue == null)
		{
			return false;
		}
		else if (!value.equals(otherValue))
		{
			return false;
		}

		return true;
	}

	public boolean isPersistable(final Object value) throws TechnicalException
	{
		return false;
	}

	@Override
	public boolean isRequired()
	{
		return required;
	}

	@Override
	public boolean isWriteable()
	{
		return writeable && accessor.isWritable();
	}

	public void setAccessor(Accessor accessor)
	{
		this.accessor = accessor;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public void setComposition(boolean composition)
	{
		this.composition = composition;
	}

	public void setEntityType(final EntityType entityType)
	{
		this.entityType = entityType;
	}

	public void setIncomingRelation(Attribute<?, ?> incomingRelation)
	{
		this.incomingRelation = (AssociationAttribute<?, ?>) incomingRelation;
	}

	public void setRequired(final boolean required)
	{
		this.required = required;
	}

	public void setTargetCardinality(Cardinality targetCardinality)
	{
		this.targetCardinality = targetCardinality;
	}

	public void setTargetType(Type<J> targetType)
	{
		this.targetType = targetType;
	}

	public void setValue(Object entity, R value)
	{
		getAccessor().setValue(entity, value);
	}

	public void setWriteable(final boolean writeable)
	{
		this.writeable = writeable;
	}

}
