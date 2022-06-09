package com.y2gcoder.blog.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.service.post.PostService;
import com.y2gcoder.blog.service.post.dto.PostCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.y2gcoder.blog.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithUserId;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}