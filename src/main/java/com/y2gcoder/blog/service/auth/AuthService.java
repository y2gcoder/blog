package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.LoginFailureException;
import com.y2gcoder.blog.exception.RoleNotFoundException;
import com.y2gcoder.blog.exception.UserEmailAlreadyExistsException;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.repository.user.RoleJpaRepository;
import com.y2gcoder.blog.repository.user.UserJpaRepository;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {
	private final UserJpaRepository userJpaRepository;
	private final RoleJpaRepository roleJpaRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@Transactional
	public void signUp(SignUpRequest req) {
		validateSignUpInfo(req);
		userJpaRepository.save(SignUpRequest.toEntity(req,
				roleJpaRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(RoleNotFoundException::new),
				passwordEncoder));
	}

	public SignInResponse signIn(SignInRequest req) {
		User user = userJpaRepository.findByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
		validatePassword(req, user);
		String subject = createSubject(user);
		String accessToken = jwtService.createAccessToken(subject);
		String refreshToken = jwtService.createRefreshToken(subject);
		return new SignInResponse(accessToken, refreshToken);
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
}
