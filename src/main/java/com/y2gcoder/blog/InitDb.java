package com.y2gcoder.blog;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.RoleNotFoundException;
import com.y2gcoder.blog.repository.user.RoleRepository;
import com.y2gcoder.blog.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Profile("local")
@RequiredArgsConstructor
@Component
public class InitDb {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		log.info("init database");
		initRole();
		initTestAdmin();
		initTestUser();
	}

	private void initRole() {
		roleRepository
				.saveAll(
						Stream.of(RoleType.values()).map(Role::new).collect(Collectors.toList())
				);
	}

	private void initTestAdmin() {
		userRepository.save(
				User.builder()
						.email("admin@admin.com")
						.password(passwordEncoder.encode("1q2w3e4r!"))
						.nickname("admin")
						.roles(roleRepository.findAll())
						.build()
		);
	}

	private void initTestUser() {
		userRepository.saveAll(
				List.of(User.builder()
								.email("user1@user.com")
								.password(passwordEncoder.encode("1q2w3e4r!"))
								.nickname("user1")
								.roles(List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(RoleNotFoundException::new)))
								.build(),
						User.builder()
								.email("user2@user.com")
								.password(passwordEncoder.encode("1q2w3e4r!"))
								.nickname("user2")
								.roles(List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(RoleNotFoundException::new)))
								.build())
		);
	}
}
