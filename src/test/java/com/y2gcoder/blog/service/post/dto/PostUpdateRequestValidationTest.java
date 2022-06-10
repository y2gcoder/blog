package com.y2gcoder.blog.service.post.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static com.y2gcoder.blog.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostUpdateRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void validateTest() {
		//given
		PostUpdateRequest req = createPostUpdateRequest("title", "content", "");

		//when
		Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isEmpty();
	}

	@Test
	void invalidateByEmptyTitleTest() {
		//given
		String invalidValue = null;
		PostUpdateRequest req = createPostUpdateRequest(invalidValue, "content", "");

		//when
		Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream()
				.map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(invalidValue);
	}

	@Test
	void invalidateByBlankTitleTest() {
		//given
		String invalidValue = "";
		PostUpdateRequest req = createPostUpdateRequest(invalidValue, "content", "");

		//when
		Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream()
				.map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(invalidValue);
	}

	@Test
	void invalidateByEmptyContentTest() {
		//given
		String invalidValue = null;
		PostUpdateRequest req = createPostUpdateRequest("title", invalidValue, "");

		//when
		Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream()
				.map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(invalidValue);
	}

	@Test
	void invalidateByBlankContentTest() {
		//given
		String invalidValue = "";
		PostUpdateRequest req = createPostUpdateRequest("title", invalidValue, "");

		//when
		Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream()
				.map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(invalidValue);
	}

	@Test
	void invalidateNotURLThumbnailUrlTest() {
		//given
		String invalidValue = "naver";
		PostUpdateRequest req = createPostUpdateRequest("title", "content", invalidValue);

		//when
		Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

		//then
		assertThat(validate).isNotEmpty();
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())
		).contains(invalidValue);
	}

}