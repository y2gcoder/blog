package com.y2gcoder.blog.service.category;

import com.y2gcoder.blog.factory.dto.CategoryCreateRequestFactory;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static com.y2gcoder.blog.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static com.y2gcoder.blog.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequestWithName;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryCreateRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void validateTest() {
		//given
		CategoryCreateRequest req = createCategoryCreateRequest();

		//when
		Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isEmpty();
	}

	@Test
	void invalidateByEmptyNameTest() {
		//given
		String invalidValue = null;
		CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);

		//when
		Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue)
				.collect(Collectors.toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByBlankNameTest() {
		//given
		String invalidValue = "";
		CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);
		//when
		Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue)
				.collect(Collectors.toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByShortNameTest() {
		//given
		String invalidValue = "c";
		CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);
		//when
		Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue)
				.collect(Collectors.toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByLongNameTest() {
		//given
		String invalidValue = "c".repeat(50);
		CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);
		//when
		Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue)
				.collect(Collectors.toSet())).contains(invalidValue);
	}
}
