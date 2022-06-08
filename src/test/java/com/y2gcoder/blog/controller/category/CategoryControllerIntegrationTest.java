package com.y2gcoder.blog.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.init.TestInitDB;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.user.UserRepository;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.category.CategoryCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.y2gcoder.blog.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static com.y2gcoder.blog.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@SpringBootTest
public class CategoryControllerIntegrationTest {
	@Autowired
	WebApplicationContext context;
	@Autowired
	MockMvc mockMvc;

	@Autowired
	TestInitDB initDB;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AuthService authService;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
		initDB.initDB();
	}

	@Test
	void findAllTest() throws Exception {
		//given, when, then
		mockMvc.perform(get("/api/categories"))
				.andExpect(status().isOk());
	}

	@Test
	void createTest() throws Exception {
		//given
		CategoryCreateRequest req = createCategoryCreateRequest();
		SignInResponse adminSignInRes = authService
				.signIn(createSignInRequest(initDB.getAdminEmail(), initDB.getPassword()));
		int beforeSize = categoryRepository.findAll().size();

		//when, then
		mockMvc.perform(
				post("/api/categories")
						.header("Authorization", adminSignInRes.getAccessToken())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isCreated());

		List<Category> result = categoryRepository.findAll();
		assertThat(result.size()).isEqualTo(beforeSize + 1);
	}

	@Test
	void createUnauthorizedByNoneTokenTest() throws Exception {
		//given
		CategoryCreateRequest req = createCategoryCreateRequest();

		//when
		mockMvc.perform(
				post("/api/categories")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isUnauthorized());
	}

	@Test
	void createAccessDeniedByNormalUserTest() throws Exception {
		//given
		CategoryCreateRequest req = createCategoryCreateRequest();
		SignInResponse normalUserSignInRes = authService.signIn(
				createSignInRequest(initDB.getUser1Email(), initDB.getPassword())
		);

		//when, then
		mockMvc.perform(
				post("/api/categories")
						.header("Authorization", normalUserSignInRes.getAccessToken())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isForbidden());
	}

	@Test
	void deleteTest() throws Exception {
		//given
		Long id = categoryRepository.findAll().get(0).getId();
		SignInResponse adminSignInRes = authService.signIn(
				createSignInRequest(initDB.getAdminEmail(), initDB.getPassword())
		);

		//when, then
		mockMvc.perform(
				delete("/api/categories/{id}", id)
						.header("Authorization", adminSignInRes.getAccessToken())
		).andExpect(status().isOk());

		List<Category> result = categoryRepository.findAll();
		assertThat(result.size()).isEqualTo(0);
	}

	@Test
	void deleteUnauthorizedByNoneTokenTest() throws Exception {
		//given
		Long id = categoryRepository.findAll().get(0).getId();

		//when, then
		mockMvc.perform(
				delete("/api/categories/{id}", id)
		).andExpect(status().isUnauthorized());
	}

	@Test
	void deleteAccessDeniedByNormalUserTest() throws Exception {
		//given
		Long id = categoryRepository.findAll().get(0).getId();
		SignInResponse normalUserSignInRes = authService.signIn(
				createSignInRequest(initDB.getUser1Email(), initDB.getPassword())
		);

		//when, then
		//when, then
		mockMvc.perform(
				delete("/api/categories/{id}", id)
						.header("Authorization", normalUserSignInRes.getAccessToken())
		).andExpect(status().isForbidden());
	}
}
