package com.y2gcoder.blog.config.token;

import com.y2gcoder.blog.handler.JwtHandler;
import lombok.RequiredArgsConstructor;

import java.util.Base64;

public class TokenHelper {

	private final JwtHandler jwtHandler;
	private final String key;
	private final long expirySeconds;

	public TokenHelper(JwtHandler jwtHandler, String key, long expirySeconds) {
		this.jwtHandler = jwtHandler;
		this.key = Base64.getEncoder().encodeToString(key.getBytes());
		this.expirySeconds = expirySeconds;
	}

	public String createToken(String subject) {
		return jwtHandler.createToken(key, subject, expirySeconds);
	}

	public boolean validate(String token) {
		return jwtHandler.validate(key, token);
	}

	public String extractSubject(String token) {
		return jwtHandler.extractSubject(key, token);
	}
}
