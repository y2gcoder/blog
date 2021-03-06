package com.y2gcoder.blog.service.user;

import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
	private final UserRepository userRepository;

	public UserDto findUser(Long id) {
		return new UserDto(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
	}

	@Transactional
	@PreAuthorize("@userGuard.check(#id)")
	public void delete(Long id) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		userRepository.delete(user);
	}


}
