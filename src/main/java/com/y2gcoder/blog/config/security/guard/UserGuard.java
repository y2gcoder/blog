package com.y2gcoder.blog.config.security.guard;

import com.y2gcoder.blog.entity.user.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserGuard {
	private final AuthHelper authHelper;

	public boolean check(Long id) {
		return authHelper.isAuthenticated() && hasAuthority(id);
	}

	private boolean hasAuthority(Long id) {
		Long userId = authHelper.extractUserId();
		Set<RoleType> userRoles = authHelper.extractUserRoles();
		return id.equals(userId) || userRoles.contains(RoleType.ROLE_ADMIN);
	}
}
