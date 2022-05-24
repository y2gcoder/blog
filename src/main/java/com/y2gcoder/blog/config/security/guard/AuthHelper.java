package com.y2gcoder.blog.config.security.guard;

import com.y2gcoder.blog.config.security.CustomAuthenticationToken;
import com.y2gcoder.blog.config.security.CustomUserDetails;
import com.y2gcoder.blog.entity.user.RoleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthHelper {
	public static boolean isAuthenticated() {
		return getAuthentication() instanceof CustomAuthenticationToken && getAuthentication().isAuthenticated();
	}

	public static Long extractUserId() {
		return Long.valueOf(getUserDetails().getUserId());
	}

	public static Set<RoleType> extractUserRoles() {
		return getUserDetails().getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.map(RoleType::valueOf)
				.collect(Collectors.toSet());
	}

	private static CustomUserDetails getUserDetails() {
		return (CustomUserDetails) getAuthentication().getPrincipal();
	}

	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
