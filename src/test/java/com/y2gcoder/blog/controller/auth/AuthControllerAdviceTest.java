package com.y2gcoder.blog.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.advice.ExceptionAdvice;
import com.y2gcoder.blog.exception.AuthenticationEntryPointException;
import com.y2gcoder.blog.exception.LoginFailureException;
import com.y2gcoder.blog.exception.RoleNotFoundException;
import com.y2gcoder.blog.exception.UserEmailAlreadyExistsException;
import com.y2gcoder.blog.factory.dto.SignInRequestFactory;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.y2gcoder.blog.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.y2gcoder.blog.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerAdviceTest {
	@InjectMocks AuthController authController;
	@Mock AuthService authService;
	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(new ExceptionAdvice()).build();
	}

	@Test
	void signInLoginFailureException() throws Exception {
		//given
		SignInRequest req = createSignInRequest("email@email.com", "1q2w3e4r!");
		given(authService.signIn(any())).willThrow(LoginFailureException.class);

		//when, then
		mockMvc.perform(
				post("/auth/sign-in")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isUnauthorized());
	}

	@Test
	void signInMethodArgumentNotValidException() throws Exception {
		//given
		SignInRequest req = createSignInRequest("email", "1234567");

		//when, then
		mockMvc.perform(
				post("/auth/sign-in")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isBadRequest());
	}

	@Test
	void signUpUserEmailAlreadyExistsException() throws Exception {
		//given
		SignUpRequest req = createSignUpRequest("email@email.com", "1q2w3e4r!", "nickname");
		doThrow(UserEmailAlreadyExistsException.class).when(authService).signUp(any());

		//when, then
		mockMvc.perform(
				post("/auth/sign-up")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isConflict());

	}

	@Test
	void signUpRoleNotFoundException() throws Exception {
		//given
		SignUpRequest req = createSignUpRequest("email@email.com", "1q2w3e4r!", "nickname");
		doThrow(RoleNotFoundException.class).when(authService).signUp(any());

		//when, then
		mockMvc.perform(
				post("/auth/sign-up")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isNotFound());
	}

	@Test
	void signUpMethodArgumentNotValidException() throws Exception {
		//given
		SignUpRequest req = createSignUpRequest("", "", "");

		//when, then
		mockMvc.perform(
				post("/auth/sign-up")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isBadRequest());
	}

	@Test
	void refreshTokenAuthenticationEntryPointException() throws Exception {
		//given
		given(authService.refreshToken(anyString())).willThrow(AuthenticationEntryPointException.class);

		//when, then
		mockMvc.perform(
						post("/auth/refresh-token")
								.header("Authorization", "refreshToken")
				).andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value(-1006));
	}

	@Test
	void refreshTokenMissingRequestHeaderException() throws Exception {
		//given, when, then
		mockMvc.perform(
						post("/auth/refresh-token")
				).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(-1008));
	}
}