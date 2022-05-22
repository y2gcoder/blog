package com.y2gcoder.blog.init;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.RoleNotFoundException;
import com.y2gcoder.blog.repository.user.RoleJpaRepository;
import com.y2gcoder.blog.repository.user.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TestInitDB {
	@Autowired
	RoleJpaRepository roleJpaRepository;
	@Autowired
	UserJpaRepository userJpaRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	private String adminEmail = "admin@admin.com";
	private String user1Email = "user1@user.com";
	private String user2Email = "user2@user.com";
	private String password = "1q2w3e4r!";

	@Transactional
	public void initDB() {
		initRole();
		initTestAdmin();
		initTestUser();
	}

	private void initRole() {
		roleJpaRepository.saveAll(
				Stream.of(RoleType.values())
						.map(Role::new).collect(Collectors.toList())
		);
	}

	private void initTestAdmin() {
		userJpaRepository.save(
				User.builder()
						.email(adminEmail)
						.password(passwordEncoder.encode(password))
						.nickname("admin")
						.roles(roleJpaRepository.findAll())
						.build()
		);
	}

	private void initTestUser() {
		userJpaRepository.saveAll(
				List.of(
						User.builder()
								.email(user1Email)
								.password(passwordEncoder.encode(password))
								.nickname("user1")
								.roles(List.of(roleJpaRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(RoleNotFoundException::new)))
								.build(),
						User.builder()
								.email(user2Email)
								.password(passwordEncoder.encode(password))
								.nickname("user2")
								.roles(List.of(roleJpaRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(RoleNotFoundException::new)))
								.build()
				)
		);
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public String getUser1Email() {
		return user1Email;
	}

	public String getUser2Email() {
		return user2Email;
	}

	public String getPassword() {
		return password;
	}
}
