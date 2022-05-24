package com.y2gcoder.blog.controller.user;

import com.y2gcoder.blog.advice.ExceptionAdvice;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.handler.FailResponseHandler;
import com.y2gcoder.blog.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerAdviceTest {
	@InjectMocks
	UserController userController;
	@Mock
	UserService userService;
	@Mock
	FailResponseHandler responseHandler;
	MockMvc mockMvc;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new ExceptionAdvice(responseHandler)).build();
	}

	@Test
	void findUserUserNotFoundExceptionTest() throws Exception {
		//given
		given(userService.findUser(anyLong())).willThrow(UserNotFoundException.class);
		//when, then
		mockMvc.perform(get("/api/users/{id}", 1L))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteUserNotFoundExceptionTest() throws Exception {
		//given
		doThrow(UserNotFoundException.class).when(userService).delete(anyLong());
		//when, then
		mockMvc.perform(
						delete("/api/users/{id}", 1L)
				).andExpect(status().isNotFound());
	}
}
