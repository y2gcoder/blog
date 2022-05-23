package com.y2gcoder.blog.config.security.guard;

import com.y2gcoder.blog.config.security.CustomAuthenticationToken;
import com.y2gcoder.blog.config.security.CustomUserDetails;
import com.y2gcoder.blog.entity.user.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthHelper {
	public boolean isAuthenticated() {
		return getAuthentication() instanceof CustomAuthenticationToken && getAuthentication().isAuthenticated();
	}

	public Long extractUserId() {
		return Long.valueOf(getUserDetails().getUserId());
	}

	public Set<RoleType> extractUserRoles() {
		return getUserDetails().getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.map(RoleType::valueOf)
				.collect(Collectors.toSet());
	}

	private CustomUserDetails getUserDetails() {
		return (CustomUserDetails) getAuthentication().getPrincipal();
	}

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
