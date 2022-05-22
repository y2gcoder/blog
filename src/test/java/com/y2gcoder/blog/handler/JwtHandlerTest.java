package com.y2gcoder.blog.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtHandlerTest {
	JwtHandler jwtHandler = new JwtHandler();

	@Test
	void createTokenTest() {
		// given, when
		String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
		String token = createToken(encodedKey, "subject", 60L);

		// then
		assertThat(token).contains("Bearer ");
	}

	@Test
	void extractSubjectTest() {
		// given
		String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
		String subject = "subject";
		String token = createToken(encodedKey, subject, 60L);

		// when
		String extractedSubject = jwtHandler.extractSubject(encodedKey, token);

		// then
		assertThat(extractedSubject).isEqualTo(subject);
	}

	@Test
	void validateTest() {
		// given
		String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
		String token = createToken(encodedKey, "subject", 60L);

		// when
		boolean isValid = jwtHandler.validate(encodedKey, token);

		// then
		assertThat(isValid).isTrue();
	}

	@Test
	void invalidateByInvalidKeyTest() {
		// given
		String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
		String token = createToken(encodedKey, "subject", 60L);

		// when
		boolean isValid = jwtHandler.validate("invalid", token);

		// then
		assertThat(isValid).isFalse();
	}

	@Test
	void invalidateByExpiredTokenTest() {
		// given
		String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
		String token = createToken(encodedKey, "subject", 0L);

		// when
		boolean isValid = jwtHandler.validate(encodedKey, token);

		// then
		assertThat(isValid).isFalse();
	}

	private String createToken(String encodedKey, String subject, long maxAgeSeconds) {
		return jwtHandler.createToken(
				encodedKey,
				subject,
				maxAgeSeconds);
	}
}