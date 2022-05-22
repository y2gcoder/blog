package com.y2gcoder.blog.service.auth.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static com.y2gcoder.blog.factory.dto.SignUpRequestFactory.*;
import static com.y2gcoder.blog.factory.dto.SignUpRequestFactory.createSignUpRequestWithNickname;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SignUpRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void validateTest() {
		// given
		SignUpRequest req = createSignUpRequest();

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isEmpty();
	}

	@Test
	void invalidateByNotFormattedEmailTest() {
		// given
		String invalidValue = "email";
		SignUpRequest req = createSignUpRequestWithEmail(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByEmptyEmailTest() {
		// given
		String invalidValue = null;
		SignUpRequest req = createSignUpRequestWithEmail(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByBlankEmailTest() {
		// given
		String invalidValue = " ";
		SignUpRequest req = createSignUpRequestWithEmail(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByEmptyPasswordTest() {
		// given
		String invalidValue = null;
		SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByBlankPasswordTest() {
		// given
		String invalidValue = "        ";
		SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByShortPasswordTest() {
		// given
		String invalidValue = "1q2w3e!";
		SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByNoneAlphabetPasswordTest() {
		// given
		String invalidValue = "123!@#123";
		SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByNoneNumberPasswordTest() {
		// given
		String invalidValue = "abc!@#abc";
		SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByNoneSpecialCasePasswordTest() {
		// given
		String invalidValue = "abc123abc";
		SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByEmptyNicknameTest() {
		// given
		String invalidValue = null;
		SignUpRequest req = createSignUpRequestWithNickname(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByBlankNicknameTest() {
		// given
		String invalidValue = " ";
		SignUpRequest req = createSignUpRequestWithNickname(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByShortNicknameTest() {
		// given
		String invalidValue = "한";
		SignUpRequest req = createSignUpRequestWithNickname(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

	@Test
	void invalidateByNotAlphabetOrHangeulNicknameTest() {
		// given
		String invalidValue = "영geun2";
		SignUpRequest req = createSignUpRequestWithNickname(invalidValue);

		// when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

		// then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
	}

}