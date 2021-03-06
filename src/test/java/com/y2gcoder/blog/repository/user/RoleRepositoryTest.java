package com.y2gcoder.blog.repository.user;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.exception.RoleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.y2gcoder.blog.factory.entity.RoleFactory.createRole;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class RoleRepositoryTest {
	@Autowired
	RoleRepository roleRepository;
	@PersistenceContext EntityManager em;

	@Test
	@DisplayName("Role:저장 성공")
	void saveRole_Normal_Success() {  //메서드명_상태_기댓값
		//given
		Role role = createRole();

		//when
		roleRepository.save(role);

		//then
		Role findRole = roleRepository.findById(role.getId()).orElseThrow(RoleNotFoundException::new);
		assertThat(findRole).isEqualTo(role);
	}

	@Test
	@DisplayName("Role:저장 실패, 중복된 Role")
	void saveRole_duplicateRole_Fail() {
		//given
		roleRepository.save(createRole());

		//when
		//then
		assertThatThrownBy(() -> roleRepository.save(createRole())).isInstanceOf(DataIntegrityViolationException.class);
	}

}