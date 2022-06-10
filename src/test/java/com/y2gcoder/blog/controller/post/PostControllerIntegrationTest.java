package com.y2gcoder.blog.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.PostNotFoundException;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.init.TestInitDB;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.post.PostRepository;
import com.y2gcoder.blog.repository.user.UserRepository;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.post.PostService;
import com.y2gcoder.blog.service.post.dto.PostCreateRequest;
import com.y2gcoder.blog.service.post.dto.PostUpdateRequest;
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
import static com.y2gcoder.blog.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static com.y2gcoder.blog.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.y2gcoder.blog.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

	@Autowired
	PostService postService;

	ObjectMapper objectMapper = new ObjectMapper();

	User user1, user2, admin;
	Category category;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		initDB.initDB();

		user1 = userRepository.findByEmail(initDB.getUser1Email()).orElseThrow(UserNotFoundException::new);
		user2 = userRepository.findByEmail(initDB.getUser2Email()).orElseThrow(UserNotFoundException::new);
		admin = userRepository.findByEmail(initDB.getAdminEmail()).orElseThrow(UserNotFoundException::new);
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

	@Test
	void deleteByResourceOwnerTest() throws Exception {
		//given
		Post post = postRepository.save(createPost(user1, category));
		SignInResponse signInResponse = authService.signIn(createSignInRequest(user1.getEmail(), initDB.getPassword()));

		//when, then
		mockMvc.perform(
				delete("/api/posts/{id}", post.getId())
						.header("Authorization", signInResponse.getAccessToken())
				)
				.andExpect(status().isOk());
		assertThatThrownBy(() -> postService.read(post.getId())).isInstanceOf(PostNotFoundException.class);
	}

	@Test
	void deleteByAdminTest() throws Exception {
		//given
		Post post = postRepository.save(createPost(user1, category));
		SignInResponse signInResponse = authService.signIn(createSignInRequest(admin.getEmail(), initDB.getPassword()));

		//when, then
		mockMvc.perform(
						delete("/api/posts/{id}", post.getId())
								.header("Authorization", signInResponse.getAccessToken())
				)
				.andExpect(status().isOk());
		assertThatThrownBy(() -> postService.read(post.getId())).isInstanceOf(PostNotFoundException.class);
	}

	@Test
	void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
		//given
		//given
		Post post = postRepository.save(createPost(user1, category));
		SignInResponse signInResponse = authService.signIn(createSignInRequest(user2.getEmail(), initDB.getPassword()));

		//when, then
		mockMvc.perform(
						delete("/api/posts/{id}", post.getId())
								.header("Authorization", signInResponse.getAccessToken())
				)
				.andExpect(status().isForbidden());
	}

	@Test
	void deleteUnauthorizedByNoneTokenTest() throws Exception {
		//given
		Post post = postRepository.save(createPost(user1, category));

		//when, then
		mockMvc.perform(
						delete("/api/posts/{id}", post.getId())
				)
				.andExpect(status().isUnauthorized());
	}

	@Test
	void updateByResourceOwnerTest() throws Exception {
		//given
		SignInResponse signInResponse = authService.signIn(createSignInRequest(user1.getEmail(), initDB.getPassword()));
		Post post = postRepository.save(createPost(user1, category));
		String updatedTitle = "updatedTitle";
		String updatedContent = "updatedContent";
		String updatedThumbnailUrl = "https://images.unsplash.com/photo-1516914357598-8c2b86ae72b6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80";
		PostUpdateRequest req = createPostUpdateRequest(updatedTitle, updatedContent, updatedThumbnailUrl);

		//when, then
		mockMvc.perform(
			put("/api/posts/{id}", post.getId())
					.header("Authorization", signInResponse.getAccessToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isOk());

		Post result = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
		assertThat(result.getTitle()).isEqualTo(updatedTitle);
		assertThat(result.getContent()).isEqualTo(updatedContent);
		assertThat(result.getThumbnailUrl()).isEqualTo(updatedThumbnailUrl);
	}

	@Test
	void updateByAdminTest() throws Exception {
		//given
		SignInResponse signInResponse = authService.signIn(createSignInRequest(admin.getEmail(), initDB.getPassword()));
		Post post = postRepository.save(createPost(user1, category));
		String updatedTitle = "updatedTitle";
		String updatedContent = "updatedContent";
		String updatedThumbnailUrl = "https://images.unsplash.com/photo-1516914357598-8c2b86ae72b6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80";
		PostUpdateRequest req = createPostUpdateRequest(updatedTitle, updatedContent, updatedThumbnailUrl);

		//when, then
		mockMvc.perform(
				put("/api/posts/{id}", post.getId())
						.header("Authorization", signInResponse.getAccessToken())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isOk());

		Post result = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
		assertThat(result.getTitle()).isEqualTo(updatedTitle);
		assertThat(result.getContent()).isEqualTo(updatedContent);
		assertThat(result.getThumbnailUrl()).isEqualTo(updatedThumbnailUrl);
	}

	@Test
	void updateUnauthorizedByNoneTokenTest() throws Exception {
		//given
		Post post = postRepository.save(createPost(user1, category));
		String updatedTitle = "updatedTitle";
		String updatedContent = "updatedContent";
		String updatedThumbnailUrl = "https://images.unsplash.com/photo-1516914357598-8c2b86ae72b6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80";
		PostUpdateRequest req = createPostUpdateRequest(updatedTitle, updatedContent, updatedThumbnailUrl);

		//when, then
		mockMvc.perform(
				put("/api/posts/{id}", post.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isUnauthorized());
	}

	@Test
	void updateAccessDeniedByNotResourceOwnerTest() throws Exception {
		//given
		SignInResponse signInResponse = authService.signIn(createSignInRequest(user2.getEmail(), initDB.getPassword()));
		Post post = postRepository.save(createPost(user1, category));
		String updatedTitle = "updatedTitle";
		String updatedContent = "updatedContent";
		String updatedThumbnailUrl = "https://images.unsplash.com/photo-1516914357598-8c2b86ae72b6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80";
		PostUpdateRequest req = createPostUpdateRequest(updatedTitle, updatedContent, updatedThumbnailUrl);

		//when, then
		mockMvc.perform(
				put("/api/posts/{id}", post.getId())
						.header("Authorization", signInResponse.getAccessToken())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isForbidden());
	}
}
