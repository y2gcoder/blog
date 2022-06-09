package com.y2gcoder.blog.controller.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.init.TestInitDB;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.post.PostRepository;
import com.y2gcoder.blog.repository.user.UserRepository;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.post.dto.PostCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.y2gcoder.blog.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static com.y2gcoder.blog.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.y2gcoder.blog.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class PostControllerIntegrationTest {
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
	PostRepository postRepository;
	@Autowired
	AuthService authService;

	ObjectMapper objectMapper = new ObjectMapper();

	User user1;
	Category category;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		initDB.initDB();
		user1 = userRepository.findByEmail(initDB.getUser1Email()).orElseThrow(UserNotFoundException::new);
		category = categoryRepository.findAll().get(0);
	}

	@Test
	void createTest() throws Exception {
		//given
		SignInResponse signInResponse = authService.signIn(createSignInRequest(user1.getEmail(), initDB.getPassword()));
		PostCreateRequest req = createPostCreateRequest("title", "content", null, category.getId(), "");

		//when
		mockMvc.perform(
				post("/api/posts")
						.header("Authorization", signInResponse.getAccessToken())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isCreated());

		Post post = postRepository.findAll().get(0);
		assertThat(post.getTitle()).isEqualTo("title");
		assertThat(post.getContent()).isEqualTo("content");
		assertThat(post.getThumbnailUrl()).isNotBlank();
		assertThat(post.getUser().getId()).isEqualTo(user1.getId());
	}

	@Test
	void createUnauthorizedByNoneTokenTest() throws Exception {
		//given
		PostCreateRequest req = createPostCreateRequest("title", "content", null, category.getId(), "");

		//when
		mockMvc.perform(
				post("/api/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isUnauthorized());
	}

	@Test
	void readTest() throws Exception {
		//given
		Post post = postRepository.save(createPost(user1, category));

		//when, then
		mockMvc.perform(
				get("/api/posts/{id}", post.getId())
		).andExpect(status().isOk());
	}
}
