package com.y2gcoder.blog.config.security;

import com.y2gcoder.blog.config.token.TokenHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class CustomUserDetailService implements UserDetailsService {
	private final TokenHelper accessTokenHelper;

	@Override
	public CustomUserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
		return accessTokenHelper.parse(token).map(this::convert).orElse(null);
	}

	private CustomUserDetails convert(TokenHelper.PrivateClaims privateClaims) {
		return new CustomUserDetails(
				privateClaims.getUserId(),
				privateClaims.getRoleTypes().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
		);
	}


}
