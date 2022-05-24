package com.y2gcoder.blog.config.security.guard;

import com.y2gcoder.blog.entity.user.RoleType;

import java.util.List;

public abstract class Guard {
	public final boolean check(Long id) {
		return AuthHelper.isAuthenticated() && (hasRole(getRoleTypes()) || isResourceOwner(id));
	}

	abstract protected List<RoleType> getRoleTypes();
	abstract protected boolean isResourceOwner(Long id);

	private boolean hasRole(List<RoleType> roleTypes) {
		return roleTypes.stream().allMatch(roleType -> AuthHelper.extractUserRoles().contains(roleType));
	}
}
