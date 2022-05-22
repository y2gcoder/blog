package com.y2gcoder.blog.factory.entity;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.RoleType;

public class RoleFactory {
	public static Role createRole() {
		return new Role(RoleType.ROLE_USER);
	}
}
