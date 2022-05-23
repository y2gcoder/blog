package com.y2gcoder.blog.config.token;

import com.y2gcoder.blog.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class TokenConfig {
	private final JwtHandler jwtHandler;

	@Bean
	public TokenHelper accessTokenHelper(
			@Value("${jwt.key.access}") String key,
			@Value("${jwt.expiry.access}") long expirySeconds
	) {
		return new TokenHelper(jwtHandler, key, expirySeconds);
	}

	@Bean
	public TokenHelper refreshTokenHelper(
			@Value("${jwt.key.refresh}") String key,
			@Value("${jwt.expiry.refresh}") long expirySeconds
	) {
		return new TokenHelper(jwtHandler, key, expirySeconds);
	}
}