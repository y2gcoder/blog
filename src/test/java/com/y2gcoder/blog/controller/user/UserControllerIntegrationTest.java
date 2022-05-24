package com.y2gcoder.blog.controller.user;

import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.init.TestInitDB;
import com.y2gcoder.blog.repository.user.UserJpaRepository;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.y2gcoder.blog.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerIntegrationTest {
	@Autowired
	WebApplicationContext context;
	@Autowired
	MockMvc mockMvc;

	@Autowired
	TestInitDB initDB;
	@Autowired
	AuthService authService;
	@Autowired
	UserJpaRepository userJpaRepository;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
		initDB.initDB();
	}

	@Test
	void findUserTest() throws Exception {
		//given
		User user = userJpaRepository
				.findByEmail(initDB.getUser1Email())
				.orElseThrow(UserNotFoundException::new);

		//when, then
		mockMvc.perform(
				get("/api/users/{id}", user.getId())
		).andExpect(status().isOk());
	}

	@Test
	void deleteTest() throws Exception {
		//given
		User user = userJpaRepository
				.findByEmail(initDB.getUser1Email())
				.orElseThrow(UserNotFoundException::new);
		SignInResponse signInResponse = authService.signIn(createSignInRequest(initDB.getUser1Email(), initDB.getPassword()));

		//when, then
		mockMvc.perform(
				delete("/api/users/{id}", user.getId()).header("Authorization", signInResponse.getAccessToken())
		).andExpect(status().isOk());
	}

	@Test
	void deleteByAdminTest() throws Exception {
		// given
		User user = userJpaRepository.findByEmail(initDB.getUser1Email()).orElseThrow(UserNotFoundException::new);
		SignInResponse adminSignInRes = authService.signIn(createSignInRequest(initDB.getAdminEmail(), initDB.getPassword()));

		// when, then
		mockMvc.perform(
						delete("/api/users/{id}", user.getId()).header("Authorization", adminSignInRes.getAccessToken()))
				.andExpect(status().isOk());
	}

	@Test
	void deleteUnauthorizedByNoneTokenTest() throws Exception {
		// given
		User user = userJpaRepository.findByEmail(initDB.getUser1Email()).orElseThrow(UserNotFoundException::new);

		// when, then
		mockMvc.perform(
						delete("/api/users/{id}", user.getId()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/exception/entry-point"));
	}

	@Test
	void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
		// given
		User user = userJpaRepository.findByEmail(initDB.getUser1Email()).orElseThrow(UserNotFoundException::new);
		SignInResponse attackerSignInRes = authService.signIn(createSignInRequest(initDB.getUser2Email(), initDB.getPassword()));

		// when, then
		mockMvc.perform(
						delete("/api/users/{id}", user.getId()).header("Authorization", attackerSignInRes.getAccessToken()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/exception/access-denied"));
	}

	@Test
	void deleteUnauthorizedByRefreshTokenTest() throws Exception {
		// given
		User user = userJpaRepository.findByEmail(initDB.getUser1Email()).orElseThrow(UserNotFoundException::new);
		SignInResponse signInResponse = authService.signIn(createSignInRequest(initDB.getUser1Email(), initDB.getPassword()));

		// when, then
		mockMvc.perform(
						delete("/api/users/{id}", user.getId()).header("Authorization", signInResponse.getRefreshToken()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/exception/entry-point"));
	}

}