/*******************************************************************************
 * Stefan Meyer, 2012 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.atemsource.atem.impl.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.apache.commons.lang.StringUtils;
import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.service.EntityProblem;
import org.atemsource.atem.api.service.EntityProblem.Type;
import org.atemsource.atem.api.service.EntityValidationService;
import org.atemsource.atem.api.type.EntityType;


public class BeanValidationService implements EntityValidationService
{

	@Resource
	private EntityTypeRepository entityTypeRepository;

	private ValidatorFactory validatorFactory;

	@PostConstruct
	public void initialize()
	{
		validatorFactory = Validation.buildDefaultValidatorFactory();
	}

	@Override
	public List<EntityProblem> validate(Object entity)
	{
		EntityType<Object> entityType = entityTypeRepository.getEntityType(entity);
		ValidatorContext validatorContext = validatorFactory.usingContext();

		Validator validator = validatorContext.getValidator();
		Class<?>[] groups = new Class[]{Default.class};
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(entity, groups);
		List<EntityProblem> entityProblems = new ArrayList<EntityProblem>();
		for (ConstraintViolation<Object> constraintViolation : constraintViolations)
		{
			final Path propertyPath = constraintViolation.getPropertyPath();

			EntityProblem entityProblem = new EntityProblem();
			entityProblem.setEntity(entity);
			entityProblem.setMessage(constraintViolation.getMessageTemplate());
			entityProblem.setType(Type.INVALID);
			entityProblems.add(entityProblem);

			// we assume that the clientIds correspond to the concatenation of containerClientId and propertypath.
			if (propertyPath == null || StringUtils.isEmpty(propertyPath.toString()))
			{
			}
			else
			{
				entityProblem.setAttribute(entityType.getAttribute(propertyPath.toString()));
			}
		}
		return entityProblems;
	}
}
