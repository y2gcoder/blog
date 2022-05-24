package com.y2gcoder.blog.config.security.guard;

import com.y2gcoder.blog.entity.user.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class UserGuard extends Guard {
	private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

	@Override
	protected List<RoleType> getRoleTypes() {
		return roleTypes;
	}

	@Override
	protected boolean isResourceOwner(Long id) {
		return id.equals(AuthHelper.extractUserId());
	}
}
