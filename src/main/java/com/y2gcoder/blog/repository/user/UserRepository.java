package com.y2gcoder.blog.repository.user;

import com.y2gcoder.blog.entity.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	@EntityGraph(attributePaths = {"roles"})
	Optional<User> findWithRolesByEmail(String email);
}
