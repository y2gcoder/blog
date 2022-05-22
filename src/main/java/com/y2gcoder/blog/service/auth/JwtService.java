package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@RequiredArgsConstructor
@Service
public class JwtService {
	private final JwtHandler jwtHandler;

	@Value("${jwt.expiry.access}")
	private long accessTokenExpirySeconds;

	@Value("${jwt.expiry.refresh}")
	private long refreshTokenExpirySeconds;

	@Value("${jwt.key.access}")
	private String accessKey;

	@Value("${jwt.key.refresh}")
	private String refreshKey;

	public String createAccessToken(String subject) {
		return jwtHandler.createToken(getEncodedKey(accessKey), subject, accessTokenExpirySeconds);
	}

	public String createRefreshToken(String subject) {
		return jwtHandler.createToken(getEncodedKey(refreshKey), subject, refreshTokenExpirySeconds);
	}

	public boolean validateAccessToken(String token) {
		return jwtHandler.validate(getEncodedKey(accessKey), token);
	}

	public boolean validateRefreshToken(String token) {
		return jwtHandler.validate(getEncodedKey(refreshKey), token);
	}

	public String extractAccessTokenSubject(String token) {
		return jwtHandler.extractSubject(getEncodedKey(accessKey), token);
	}

	public String extractRefreshTokenSubject(String token) {
		return jwtHandler.extractSubject(getEncodedKey(refreshKey), token);
	}

	private String getEncodedKey(String key) {
		return Base64.getEncoder().encodeToString(key.getBytes());
	}
}
