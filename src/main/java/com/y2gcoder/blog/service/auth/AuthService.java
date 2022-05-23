package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.config.token.TokenHelper;
import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.*;
import com.y2gcoder.blog.repository.user.RoleJpaRepository;
import com.y2gcoder.blog.repository.user.UserJpaRepository;
import com.y2gcoder.blog.service.auth.dto.RefreshTokenResponse;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
	private final UserJpaRepository userJpaRepository;
	private final RoleJpaRepository roleJpaRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenHelper accessTokenHelper;
	private final TokenHelper refreshTokenHelper;

	@Transactional
	public void signUp(SignUpRequest req) {
		validateSignUpInfo(req);
		userJpaRepository.save(SignUpRequest.toEntity(req,
				roleJpaRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(RoleNotFoundException::new),
				passwordEncoder));
	}

	@Transactional(readOnly = true)
	public SignInResponse signIn(SignInRequest req) {
		User user = userJpaRepository.findByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
		validatePassword(req, user);
		String subject = createSubject(user);
		String accessToken = accessTokenHelper.createToken(subject);
		String refreshToken = refreshTokenHelper.createToken(subject);
		return new SignInResponse(accessToken, refreshToken);
	}

	public RefreshTokenResponse refreshToken(String refreshToken) {
		validateRefreshToken(refreshToken);
		String subject = refreshTokenHelper.extractSubject(refreshToken);
		String accessToken = accessTokenHelper.createToken(subject);
		return new RefreshTokenResponse(accessToken);
	}

	private void validateSignUpInfo(SignUpRequest req) {
		if(userJpaRepository.existsByEmail(req.getEmail()))
			throw new UserEmailAlreadyExistsException(req.getEmail());
	}

	private void validatePassword(SignInRequest req, User user) {
		if(!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
			throw new LoginFailureException();
		}
	}

	private String createSubject(User user) {
		return String.valueOf(user.getId());
	}


	private void validateRefreshToken(String refreshToken) {
		if (!refreshTokenHelper.validate(refreshToken)) {
			throw new AuthenticationEntryPointException();
		}
	}
}
