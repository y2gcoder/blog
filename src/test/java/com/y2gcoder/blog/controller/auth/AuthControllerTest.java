package com.y2gcoder.blog.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
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
import static com.y2gcoder.blog.factory.dto.SignInResponseFactory.createSignInResponse;
import static com.y2gcoder.blog.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
	@InjectMocks AuthController authController;
	@Mock AuthService authService;
	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	}

	@Test
	void signUpTest() throws Exception {
		// given
		SignUpRequest req = createSignUpRequest("email@email.com", "1q2w3e4r!", "nickname");

		// when, then
		mockMvc.perform(
						post("/auth/sign-up")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isCreated());

		verify(authService).signUp(req);
	}

	@Test
	void signInTest() throws Exception {
		// given
		SignInRequest req = createSignInRequest("email@email.com", "1q2w3e4r!");
		given(authService.signIn(req)).willReturn(createSignInResponse("access", "refresh"));

		// when, then
		mockMvc.perform(
						post("/auth/sign-in")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.data.accessToken").value("access"))
				.andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));

		verify(authService).signIn(req);
	}

	@Test
	void ignoreNullValueInJsonResponseTest() throws Exception { // 4
		// given
		SignUpRequest req = createSignUpRequest("email@email.com", "123456a!", "nickname");

		// when, then
		mockMvc.perform(
						post("/auth/sign-up")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.result").doesNotExist());

	}

}