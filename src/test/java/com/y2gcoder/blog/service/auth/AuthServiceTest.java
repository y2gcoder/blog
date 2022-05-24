package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.config.token.TokenHelper;
import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.exception.*;
import com.y2gcoder.blog.repository.user.RoleRepository;
import com.y2gcoder.blog.repository.user.UserRepository;
import com.y2gcoder.blog.service.auth.dto.RefreshTokenResponse;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.y2gcoder.blog.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.y2gcoder.blog.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static com.y2gcoder.blog.factory.entity.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	AuthService authService;
	@Mock
	UserRepository userRepository;
	@Mock
	RoleRepository roleRepository;
	@Mock
	PasswordEncoder passwordEncoder;
	@Mock
	TokenHelper accessTokenHelper;
	@Mock
	TokenHelper refreshTokenHelper;

	@BeforeEach
	void beforeEach() {
		authService = new AuthService(
				userRepository,
				roleRepository,
				passwordEncoder,
				accessTokenHelper,
				refreshTokenHelper
		);
	}

	@Test
	void signUpTest() {
		// given
		SignUpRequest req = createSignUpRequest();
		given(roleRepository.findByRoleType(RoleType.ROLE_USER)).willReturn(Optional.of(new Role(RoleType.ROLE_USER)));

		// when
		authService.signUp(req);

		// then
		verify(passwordEncoder).encode(req.getPassword());
		verify(userRepository).save(any());
	}

	@Test
	void validateSignUpByDuplicateEmailTest() {
		// given
		given(userRepository.existsByEmail(anyString())).willReturn(true);

		// when, then
		assertThatThrownBy(() -> authService.signUp(createSignUpRequest()))
				.isInstanceOf(UserEmailAlreadyExistsException.class);
	}

	@Test
	void signUpRoleNotFoundTest() {
		// given
		given(roleRepository.findByRoleType(RoleType.ROLE_USER)).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> authService.signUp(createSignUpRequest()))
				.isInstanceOf(RoleNotFoundException.class);
	}

	@Test
	void signInTest() {
		// given
		given(userRepository.findWithRolesByEmail(any())).willReturn(Optional.of(createUser()));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
		given(accessTokenHelper.createToken(any())).willReturn("access");
		given(refreshTokenHelper.createToken(any())).willReturn("refresh");

		// when
		SignInResponse res = authService.signIn(createSignInRequest("email", "password"));

		// then
		assertThat(res.getAccessToken()).isEqualTo("access");
		assertThat(res.getRefreshToken()).isEqualTo("refresh");
	}

	@Test
	void signInExceptionByNoneUserTest() {
		// given
		given(userRepository.findWithRolesByEmail(any())).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> authService.signIn(createSignInRequest("email", "password")))
				.isInstanceOf(LoginFailureException.class);
	}

	@Test
	void signInExceptionByInvalidPasswordTest() {
		// given
		given(userRepository.findWithRolesByEmail(any())).willReturn(Optional.of(createUser()));
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
		given(refreshTokenHelper.parse(refreshToken))
				.willReturn(Optional.of(new TokenHelper.PrivateClaims("userId", List.of("ROLE_USER"))));
		given(accessTokenHelper.createToken(any())).willReturn(accessToken);

		//when
		RefreshTokenResponse response = authService.refreshToken(refreshToken);

		//then
		assertThat(response.getAccessToken()).isEqualTo(accessToken);
	}

	@Test
	void refreshTokenExceptionByInvalidTokenTest() {
		//given
		String refreshToken = "refreshToken";
		given(refreshTokenHelper.parse(refreshToken)).willReturn(Optional.empty());

		//when, then
		assertThatThrownBy(() -> authService.refreshToken(refreshToken))
				.isInstanceOf(RefreshTokenFailureException.class);
	}

}