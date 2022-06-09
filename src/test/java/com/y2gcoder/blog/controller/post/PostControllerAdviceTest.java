package com.y2gcoder.blog.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.advice.ExceptionAdvice;
import com.y2gcoder.blog.exception.CategoryNotFoundException;
import com.y2gcoder.blog.exception.PostNotFoundException;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.handler.FailResponseHandler;
import com.y2gcoder.blog.service.post.PostService;
import com.y2gcoder.blog.service.post.dto.PostCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.y2gcoder.blog.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithUserId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostControllerAdviceTest {
	@InjectMocks PostController postController;
	@Mock
	PostService postService;
	@Mock
	FailResponseHandler responseHandler;
	MockMvc mockMvc;

	@BeforeEach
	void beforeEach() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("i18n/exception");
		mockMvc = MockMvcBuilders
				.standaloneSetup(postController)
				.setControllerAdvice(new ExceptionAdvice(responseHandler))
				.build();
	}

	@Test
	void createExceptionByUserNotFoundException() throws Exception {
		//given
		given(postService.create(any())).willThrow(UserNotFoundException.class);

		//when, then
		performCreate().andExpect(status().isNotFound());
	}

	@Test
	void createExceptionByCategoryNotFoundException() throws Exception {
		//given
		given(postService.create(any())).willThrow(CategoryNotFoundException.class);

		//when, then
		performCreate().andExpect(status().isNotFound());
	}

	private ResultActions performCreate() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		PostCreateRequest req = createPostCreateRequestWithUserId(null);
		return mockMvc.perform(
				post("/api/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		);
	}

	@Test
	void readExceptionByPostNotFoundTest() throws Exception {
		//given
		given(postService.read(anyLong())).willThrow(PostNotFoundException.class);

		//when, then
		mockMvc.perform(
				get("/api/posts/{id}", 1L)
		).andExpect(status().isNotFound());
	}
}
