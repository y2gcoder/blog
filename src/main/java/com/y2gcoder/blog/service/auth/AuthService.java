package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.config.token.TokenHelper;
import com.y2gcoder.blog.entity.user.Role;
import com.y2gcoder.blog.entity.user.RoleType;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.entity.user.UserRole;
import com.y2gcoder.blog.exception.*;
import com.y2gcoder.blog.repository.user.RoleRepository;
import com.y2gcoder.blog.repository.user.UserRepository;
import com.y2gcoder.blog.service.auth.dto.RefreshTokenResponse;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenHelper accessTokenHelper;
	private final TokenHelper refreshTokenHelper;

	@Transactional
	public void signUp(SignUpRequest req) {
		validateSignUpInfo(req);
		userRepository.save(SignUpRequest.toEntity(req,
				roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(RoleNotFoundException::new),
				passwordEncoder));
	}

	@Transactional(readOnly = true)
	public SignInResponse signIn(SignInRequest req) {
		User user = userRepository.findWithRolesByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
		validatePassword(req, user);
		TokenHelper.PrivateClaims privateClaims = createPrivateClaims(user);
		String accessToken = accessTokenHelper.createToken(privateClaims);
		String refreshToken = refreshTokenHelper.createToken(privateClaims);
		return new SignInResponse(accessToken, refreshToken);
	}

	public RefreshTokenResponse refreshToken(String refreshToken) {
		TokenHelper.PrivateClaims privateClaims = refreshTokenHelper.parse(refreshToken).orElseThrow(RefreshTokenFailureException::new);
		String accessToken = accessTokenHelper.createToken(privateClaims);
		return new RefreshTokenResponse(accessToken);
	}

	private void validateSignUpInfo(SignUpRequest req) {
		if(userRepository.existsByEmail(req.getEmail()))
			throw new UserEmailAlreadyExistsException(req.getEmail());
	}

	private void validatePassword(SignInRequest req, User user) {
		if(!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
			throw new LoginFailureException();
		}
	}

	private TokenHelper.PrivateClaims createPrivateClaims(User user) {
		return new TokenHelper.PrivateClaims(
				String.valueOf(user.getId()),
				user.getRoles().stream()
						.map(UserRole::getRole)
						.map(Role::getRoleType)
						.map(Enum::toString)
						.collect(Collectors.toList())
		);
	}
}
