package com.y2gcoder.blog.config.security;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.entity.user.UserRole;
import com.y2gcoder.blog.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class CustomUserDetailService implements UserDetailsService {
	private final UserJpaRepository userJpaRepository;

	@Override
	public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = userJpaRepository.findById(Long.valueOf(userId))
				.orElseGet(() -> User.builder()
						.email(null)
						.password(null)
						.nickname(null)
						.roles(Collections.emptyList())
						.build());
		return new CustomUserDetails(
				String.valueOf(user.getId()),
				user.getRoles().stream().map(UserRole::getRole)
						.map(Role::getRoleType)
						.map(Enum::toString)
						.map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
		);
	}
}
