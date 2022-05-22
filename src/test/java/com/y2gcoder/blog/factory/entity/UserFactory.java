package com.y2gcoder.blog.factory.entity;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.User;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public class UserFactory {

	public static User createUser() {
		return User.builder()
				.email("email@email.com")
				.password("1q2w3e4r!")
				.nickname("nickname")
				.roles(emptyList())
				.build();
	}

	public static User createUser(String email, String password, String nickname) {
		return User.builder()
				.email(email)
				.password(password)
				.nickname(nickname)
				.roles(emptyList())
				.build();
	}

	public static User createUserWithRoles(List<Role> roles) {
		return User.builder()
				.email("email@email.com")
				.password("1q2w3e4r!")
				.nickname("nickname")
				.roles(roles)
				.build();
	}
}
