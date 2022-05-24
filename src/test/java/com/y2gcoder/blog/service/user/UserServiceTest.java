package com.y2gcoder.blog.service.user;

import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.y2gcoder.blog.factory.entity.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@InjectMocks UserService userService;
	@Mock
	UserRepository userRepository;

	@Test
	void findUserTest() {
		//given
		User user = createUser();
		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

		//when
		UserDto result = userService.findUser(1L);

		//then
		assertThat(result.getEmail()).isEqualTo(user.getEmail());
	}

	@Test
	void findUserUserNotFoundExceptionTest() {
		//given
		given(userRepository.findById(anyLong())).willReturn(Optional.empty());
		//when, then
		assertThatThrownBy(() -> userService.findUser(1L)).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void deleteTest() {
		//given
		given(userRepository.findById(anyLong())).willReturn(Optional.of(createUser()));
		//when
		userService.delete(1L);
		//then
		verify(userRepository).delete(any());
	}

	@Test
	void deleteUserNotFoundExceptionTest() {
		//given
		given(userRepository.findById(anyLong())).willReturn(Optional.empty());
		//when, then
		assertThatThrownBy(() -> userService.delete(1L)).isInstanceOf(UserNotFoundException.class);
	}

}