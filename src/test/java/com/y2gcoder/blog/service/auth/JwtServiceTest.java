package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
	@InjectMocks JwtService jwtService;
	@Mock JwtHandler jwtHandler;

	@BeforeEach
	void beforeEach() {
		ReflectionTestUtils.setField(jwtService, "accessTokenExpirySeconds", 10L);
		ReflectionTestUtils.setField(jwtService, "refreshTokenExpirySeconds", 10L);
		ReflectionTestUtils.setField(jwtService, "accessKey", "accessKey");
		ReflectionTestUtils.setField(jwtService, "refreshKey", "refreshKey");
	}

	@Test
	void createAccessTokenTest() {
		// given
		given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("access");

		// when
		String token = jwtService.createAccessToken("subject");

		// then
		assertThat(token).isEqualTo("access");
		verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
	}

	@Test
	void createRefreshTokenTest() {
		// given
		given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("refresh");

		// when
		String token = jwtService.createRefreshToken("subject");

		// then
		assertThat(token).isEqualTo("refresh");
		verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
	}

	@Test
	void validateAccessTokenTest() {
		//given
		given(jwtHandler.validate(anyString(), anyString())).willReturn(true);
		//when, then
		assertThat(jwtService.validateAccessToken("token")).isTrue();
	}

	@Test
	void invalidateAccessTokenTest() {
		//given
		given(jwtHandler.validate(anyString(), anyString())).willReturn(false);
		//when, then
		assertThat(jwtService.validateAccessToken("token")).isFalse();
	}

	@Test
	void validateRefreshTokenTest() {
		//given
		given(jwtHandler.validate(anyString(), anyString())).willReturn(true);
		//when, then
		assertThat(jwtService.validateRefreshToken("token")).isTrue();
	}

	@Test
	void invalidateRefreshTokenTest() {
		//given
		given(jwtHandler.validate(anyString(), anyString())).willReturn(false);
		//when, then
		assertThat(jwtService.validateRefreshToken("token")).isFalse();
	}

	@Test
	void extractAccessTokenSubjectTest() {
		//given
		String subject = "subject";
		given(jwtHandler.extractSubject(anyString(), anyString())).willReturn(subject);

		//when
		String result = jwtService.extractAccessTokenSubject("token");

		//then
		assertThat(subject).isEqualTo(result);
	}

	@Test
	void extractRefreshTokenSubjectTest() {
		//given
		String subject = "subject";
		given(jwtHandler.extractSubject(anyString(), anyString())).willReturn(subject);

		//when
		String result = jwtService.extractRefreshTokenSubject("token");

		//then
		assertThat(subject).isEqualTo(result);
	}
}