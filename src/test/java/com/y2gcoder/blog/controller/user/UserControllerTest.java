package com.y2gcoder.blog.controller.user;

import com.y2gcoder.blog.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	@InjectMocks UserController userController;
	@Mock
	UserService userService;
	MockMvc mockMvc;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	void findUserTest() throws Exception {
		//given
		Long id = 1L;

		//when, then
		mockMvc.perform(get("/users/{id}", id)).andExpect(status().isOk());
		verify(userService).findUser(id);
	}

	@Test
	void deleteTest() throws Exception {
		//given
		Long id = 1L;

		//when, then
		mockMvc.perform(delete("/users/{id}", id)).andExpect(status().isOk());
		verify(userService).delete(id);
	}
}