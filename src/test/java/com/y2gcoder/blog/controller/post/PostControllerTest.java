package com.y2gcoder.blog.controller.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.repository.post.dto.PostReadCondition;
import com.y2gcoder.blog.service.post.PostService;
import com.y2gcoder.blog.service.post.dto.PostCreateRequest;
import com.y2gcoder.blog.service.post.dto.PostUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.y2gcoder.blog.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithUserId;
import static com.y2gcoder.blog.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static com.y2gcoder.blog.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {
	@InjectMocks PostController postController;
	@Mock
	PostService postService;
	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
	}

	@Test
	void createTest() throws Exception {
		//given
		PostCreateRequest req = createPostCreateRequestWithUserId(null);

		//when, then
		mockMvc.perform(
				post("/api/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isCreated());

		verify(postService).create(req);
	}

	@Test
	void readTest() throws Exception {
		//given
		Long id = 1L;

		//when, then
		mockMvc.perform(
			get("/api/posts/{id}",id)
		).andExpect(status().isOk());

		verify(postService).read(id);
	}

	@Test
	void deleteTest() throws Exception {
		//given
		Long id = 1L;

		//when, then
		mockMvc.perform(
			delete("/api/posts/{id}", id)
		).andExpect(status().isOk());

		verify(postService).delete(id);
	}

	@Test
	void updateTest() throws Exception {
		//given
		PostUpdateRequest req = createPostUpdateRequest(
				"Updated Title",
				"Updated Content",
				"https://images.unsplash.com/photo-1516914357598-8c2b86ae72b6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
		);

		//when, then
		mockMvc.perform(
			put("/api/posts/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isOk());

		verify(postService).update(anyLong(), eq(req));
	}

	@Test
	void readAllTest() throws Exception {
		//given
		PostReadCondition condition = createPostReadCondition(1, null, List.of(1L, 2L));

		//when, then
		mockMvc.perform(
			get("/api/posts")
					.param("size", String.valueOf(condition.getSize()))
					.param(
							"categoryIds",
							String.valueOf(condition.getCategoryIds().get(0)),
							String.valueOf(condition.getCategoryIds().get(1))
					)
		).andExpect(status().isOk());

		verify(postService).readAll(condition);

	}
}