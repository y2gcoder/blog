package com.y2gcoder.blog.repository.user;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.entity.user.UserRole;
import com.y2gcoder.blog.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.y2gcoder.blog.factory.entity.UserFactory.createUser;
import static com.y2gcoder.blog.factory.entity.UserFactory.createUserWithRoles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserJpaRepositoryTest {
	@Autowired UserJpaRepository userJpaRepository;
	@Autowired RoleJpaRepository roleJpaRepository;
	@PersistenceContext EntityManager em;

	@Test
	@DisplayName("User:저장 성공")
	void saveUser_Normal_Success() {
		//given
		User user = createUser();
		//when
		userJpaRepository.save(user);
		//then
		User findUser = userJpaRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
		assertThat(findUser).isEqualTo(user);
	}

	@Test
	@DisplayName("User:저장 실패, 중복된 email")
	void saveUser_duplicateEmail_Fail() {
		//given
		userJpaRepository.save(createUser());
		//when
		//then
		assertThatThrownBy(() -> userJpaRepository.save(createUser())).isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	@DisplayName("User:nickname 변경 성공")
	void changeNickname_Normal_Success() {
		//given
		User user = createUser();
		userJpaRepository.save(user);
		flushAndClear();
		//when
		User findUser = userJpaRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
		String nickname = "바꾼닉네임";
		findUser.changeNickname(nickname);
		flushAndClear();
		//then
		User findUser2 = userJpaRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
		assertThat(findUser2.getNickname()).isEqualTo(nickname);
	}

	@Test
	@DisplayName("User:profile 변경 성공")
	void changeProfile_Normal_Success() {
		//given
		User user = createUser();
		userJpaRepository.save(user);
		flushAndClear();
		//when
		User findUser = userJpaRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
		String profile = "프로필";
		findUser.changeProfile(profile);
		flushAndClear();
		//then
		User findUser2 = userJpaRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
		assertThat(findUser2.getProfile()).isEqualTo(profile);
	}

	@Test
	@DisplayName("User:저장시 userRole 같이 저장")
	void saveUserWithRoles_Normal_Success() {
		//given
		List<RoleType> roleTypes = List.of(RoleType.ROLE_USER, RoleType.ROLE_ADMIN);
		List<Role> roles = roleTypes.stream().map(Role::new).collect(Collectors.toList());
		roleJpaRepository.saveAll(roles);
		//when
		User savedUser = userJpaRepository.save(createUserWithRoles(roleJpaRepository.findAll()));
		//then
		User findUser = userJpaRepository.findById(savedUser.getId()).orElseThrow(UserNotFoundException::new);
		assertThat(findUser.getRoles().size()).isEqualTo(roles.size());
	}

	@Test
	@DisplayName("User:삭제시 userRole 같이 삭제")
	void deleteUserWithRoles_Normal_Success() {
		//given
		List<RoleType> roleTypes = List.of(RoleType.ROLE_USER, RoleType.ROLE_ADMIN);
		List<Role> roles = roleTypes.stream().map(Role::new).collect(Collectors.toList());
		roleJpaRepository.saveAll(roles);
		User savedUser = userJpaRepository.save(createUserWithRoles(roleJpaRepository.findAll()));
		//when
		userJpaRepository.delete(savedUser);
		flushAndClear();
		//then
		List<UserRole> resultList = em.createQuery("select ur from UserRole ur", UserRole.class).getResultList();
		assertThat(resultList.size()).isZero();
	}

	private void flushAndClear() {
		em.flush();
		em.clear();
	}
}