package com.y2gcoder.blog.repository.user;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRoleType(RoleType roleType);
}
