package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.AuthenticationEntryPointException;
import com.y2gcoder.blog.exception.LoginFailureException;
import com.y2gcoder.blog.exception.RoleNotFoundException;
import com.y2gcoder.blog.exception.UserEmailAlreadyExistsException;
import com.y2gcoder.blog.repository.user.RoleJpaRepository;
import com.y2gcoder.blog.repository.user.UserJpaRepository;
import com.y2gcoder.blog.service.auth.dto.RefreshTokenResponse;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static com.y2gcoder.blog.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.y2gcoder.blog.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static com.y2gcoder.blog.factory.entity.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	@InjectMocks AuthService authService;
	@Mock UserJpaRepository userJpaRepository;
	@Mock RoleJpaRepository roleJpaRepository;
	@Mock PasswordEncoder passwordEncoder;
	@Mock JwtService jwtService;

	@Test
	void signUpTest() {
		// given
		SignUpRequest req = createSignUpRequest();
		given(roleJpaRepository.findByRoleType(RoleType.ROLE_USER)).willReturn(Optional.of(new Role(RoleType.ROLE_USER)));

		// when
		authService.signUp(req);

		// then
		verify(passwordEncoder).encode(req.getPassword());
		verify(userJpaRepository).save(any());
	}

	@Test
	void validateSignUpByDuplicateEmailTest() {
		// given
		given(userJpaRepository.existsByEmail(anyString())).willReturn(true);

		// when, then
		assertThatThrownBy(() -> authService.signUp(createSignUpRequest()))
				.isInstanceOf(UserEmailAlreadyExistsException.class);
	}

	@Test
	void signUpRoleNotFoundTest() {
		// given
		given(roleJpaRepository.findByRoleType(RoleType.ROLE_USER)).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> authService.signUp(createSignUpRequest()))
				.isInstanceOf(RoleNotFoundException.class);
	}

	@Test
	void signInTest() {
		// given
		given(userJpaRepository.findByEmail(any())).willReturn(Optional.of(createUser()));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
		given(jwtService.createAccessToken(anyString())).willReturn("access");
		given(jwtService.createRefreshToken(anyString())).willReturn("refresh");

		// when
		SignInResponse res = authService.signIn(createSignInRequest("email", "password"));

		// then
		assertThat(res.getAccessToken()).isEqualTo("access");
		assertThat(res.getRefreshToken()).isEqualTo("refresh");
	}

	@Test
	void signInExceptionByNoneMemberTest() {
		// given
		given(userJpaRepository.findByEmail(any())).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> authService.signIn(createSignInRequest("email", "password")))
				.isInstanceOf(LoginFailureException.class);
	}

	@Test
	void signInExceptionByInvalidPasswordTest() {
		// given
		given(userJpaRepository.findByEmail(any())).willReturn(Optional.of(createUser()));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

		// when, then
		assertThatThrownBy(() -> authService.signIn(createSignInRequest("email", "password")))
				.isInstanceOf(LoginFailureException.class);
	}

	@Test
	void refreshTokenTest() {
		//given
		String refreshToken = "refreshToken";
		String subject = "subject";
		String accessToken = "accessToken";
		given(jwtService.validateRefreshToken(refreshToken)).willReturn(true);
		given(jwtService.extractRefreshTokenSubject(refreshToken)).willReturn(subject);
		given(jwtService.createAccessToken(subject)).willReturn(accessToken);

		//when
		RefreshTokenResponse response = authService.refreshToken(refreshToken);

		//then
		assertThat(response.getAccessToken()).isEqualTo(accessToken);
	}

	@Test
	void refreshTokenExceptionByInvalidTokenTest() {
		//given
		String refreshToken = "refreshToken";
		given(jwtService.validateRefreshToken(refreshToken)).willReturn(false);

		//when, then
		assertThatThrownBy(() -> authService.refreshToken(refreshToken))
				.isInstanceOf(AuthenticationEntryPointException.class);
	}

}