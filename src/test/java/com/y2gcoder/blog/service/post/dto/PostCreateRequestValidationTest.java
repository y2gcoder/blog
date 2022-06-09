package com.y2gcoder.blog.service.post.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static com.y2gcoder.blog.factory.dto.PostCreateRequestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostCreateRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void validationTest() {
		//given
		PostCreateRequest req = createPostCreateRequestWithUserId(null);
		//when
		Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isEmpty();
	}

	@Test
	void invalidateByEmptyTitleTest() {
		//given
		String invalidValue = null;
		PostCreateRequest req = createPostCreateRequestWithTitle(invalidValue);

		//when
		Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())
		).contains(invalidValue);
	}

	@Test
	void invalidateByBlankTitleTest() {
		//given
		String invalidValue = "";
		PostCreateRequest req = createPostCreateRequestWithTitle(invalidValue);

		//when
		Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())
		).contains(invalidValue);
	}

	@Test
	void invalidateByEmptyContentTest() {
		//given
		String invalidValue = null;
		PostCreateRequest req = createPostCreateRequestWithContent(invalidValue);

		//when
		Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())
		).contains(invalidValue);
	}

	@Test
	void invalidateByBlankContentTest() {
		//given
		String invalidValue = "";
		PostCreateRequest req = createPostCreateRequestWithContent(invalidValue);

		//when
		Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())
		).contains(invalidValue);
	}

	@Test
	void invalidateByNotNullUserIdTest() {
		//given
		Long invalidValue = 1L;
		PostCreateRequest req = createPostCreateRequestWithUserId(invalidValue);

		//when
		Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())
		).contains(invalidValue);
	}

	@Test
	void invalidateByNullCategoryIdTest() {
		//given
		Long invalidValue = null;
		PostCreateRequest req = createPostCreateRequestWithCategoryId(invalidValue);

		//when
		Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())
		).contains(invalidValue);
	}

	@Test
	void invalidateNegativeCategoryIdTest() {
		//given
		Long invalidValue = -1L;
		PostCreateRequest req = createPostCreateRequestWithCategoryId(invalidValue);

		//when
		Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())
		).contains(invalidValue);
	}

	@Test
	void invalidateNotURLThumbnailUrlTest() {
		//given
		String invalidValue = "naver";
		PostCreateRequest req = createPostCreateRequestWithThumbnailUrl(invalidValue);

		//when
		Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())
		).contains(invalidValue);
	}

}