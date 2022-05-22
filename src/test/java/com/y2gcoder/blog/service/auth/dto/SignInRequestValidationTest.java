package com.y2gcoder.blog.service.auth.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static com.y2gcoder.blog.factory.dto.SignInRequestFactory.*;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SignInRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void validateTest() {
		// given
		SignInRequest req = createSignInRequest();

		// when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req); // 2

		// then
		assertThat(validate).isEmpty(); // 3
	}

	@Test
	void invalidateByNotFormattedEmailTest() {
		// given
		String invalidValue = "email";
		SignInRequest req = createSignInRequestWithEmail(invalidValue);

		// when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty(); // 4
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByEmptyEmailTest() {
		// given
		String invalidValue = null;
		SignInRequest req = createSignInRequestWithEmail(invalidValue);

		// when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByBlankEmailTest() {
		// given
		String invalidValue = " ";
		SignInRequest req = createSignInRequestWithEmail(invalidValue);

		// when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByEmptyPasswordTest() {
		// given
		String invalidValue = null;
		SignInRequest req = createSignInRequestWithPassword(invalidValue);

		// when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByBlankPasswordTest() {
		// given
		String invalidValue = " ";
		SignInRequest req = createSignInRequestWithPassword(invalidValue);

		// when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

}