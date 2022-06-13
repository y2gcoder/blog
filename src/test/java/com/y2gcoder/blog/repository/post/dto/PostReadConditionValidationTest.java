package com.y2gcoder.blog.repository.post.dto;

import com.y2gcoder.blog.repository.post.dto.PostReadCondition;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static com.y2gcoder.blog.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static org.assertj.core.api.Assertions.assertThat;

class PostReadConditionValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void validateTest() {
		//given
		PostReadCondition condition = createPostReadCondition(1, null);
		//when
		Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(condition);
		//then
		assertThat(validate).isEmpty();
	}

	@Test
	void invalidateByNullSizeTest() {
		//given
		Integer invalidValue = null;
		PostReadCondition condition = createPostReadCondition(invalidValue, null);

		//when
		Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(condition);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(invalidValue);
	}

	@Test
	void invalidateByNegativeOrZeroSizeTest() {
		//given
		Integer invalidValue = 0;
		PostReadCondition condition = createPostReadCondition(invalidValue, null);

		//when
		Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(condition);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(invalidValue);
	}

	@Test
	void invalidateByNegativeOrZeroLastPostIdTest() {
		//given
		Long invalidValue = 0L;
		PostReadCondition condition = createPostReadCondition(1, invalidValue);

		//when
		Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(condition);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(invalidValue);
	}
}