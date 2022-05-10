package com.y2gcoder.blog.repository.user;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.exception.RoleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class RoleJpaRepositoryTest {
	@Autowired RoleJpaRepository roleJpaRepository;
	@PersistenceContext EntityManager em;

	@Test
	@DisplayName("Role:저장 성공")
	void saveRole_Normal_Success() {  //메서드명_상태_기댓값
		//given
		Role role = createTestRole();

		//when
		roleJpaRepository.save(role);

		//then
		Role findRole = roleJpaRepository.findById(role.getId()).orElseThrow(RoleNotFoundException::new);
		assertThat(findRole).isEqualTo(role);
	}

	@Test
	@DisplayName("Role:저장 실패, 중복된 Role")
	void saveRole_duplicateRole_Fail() {
		//given
		roleJpaRepository.save(createTestRole());

		//when
		//then
		assertThatThrownBy(() -> roleJpaRepository.save(createTestRole())).isInstanceOf(DataIntegrityViolationException.class);
	}

	private Role createTestRole() {
		return new Role(RoleType.ROLE_USER);
	}

}