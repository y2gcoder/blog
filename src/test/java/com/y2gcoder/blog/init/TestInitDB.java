package com.y2gcoder.blog.init;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.RoleNotFoundException;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.user.RoleRepository;
import com.y2gcoder.blog.repository.user.UserRepository;
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
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	CategoryRepository categoryRepository;

	private String adminEmail = "admin@admin.com";
	private String user1Email = "user1@user.com";
	private String user2Email = "user2@user.com";
	private String password = "1q2w3e4r!";

	@Transactional
	public void initDB() {
		initRole();
		initTestAdmin();
		initTestUser();
		initCategory();
	}

	private void initRole() {
		roleRepository.saveAll(
				Stream.of(RoleType.values())
						.map(Role::new).collect(Collectors.toList())
		);
	}

	private void initTestAdmin() {
		userRepository.save(
				User.builder()
						.email(adminEmail)
						.password(passwordEncoder.encode(password))
						.nickname("admin")
						.roles(roleRepository.findAll())
						.build()
		);
	}

	private void initTestUser() {
		userRepository.saveAll(
				List.of(
						User.builder()
								.email(user1Email)
								.password(passwordEncoder.encode(password))
								.nickname("user1")
								.roles(List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(RoleNotFoundException::new)))
								.build(),
						User.builder()
								.email(user2Email)
								.password(passwordEncoder.encode(password))
								.nickname("user2")
								.roles(List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(RoleNotFoundException::new)))
								.build()
				)
		);
	}

	private void initCategory() {
		Category category1 = new Category("category1", null);
		Category category2 = new Category("category2", category1);
		categoryRepository.saveAll(List.of(category1, category2));
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
