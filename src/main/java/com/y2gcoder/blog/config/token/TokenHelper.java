package com.y2gcoder.blog.config.token;

import com.y2gcoder.blog.handler.JwtHandler;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TokenHelper {
	private final JwtHandler jwtHandler;
	private final String key;
	private final long expirySeconds;

	private static final String SEPARATOR = ",";
	private static final String ROLE_TYPES = "ROLE_TYPES";
	private static final String USER_ID = "USER_ID";

	public String createToken(PrivateClaims privateClaims) {
		return jwtHandler.createToken(
				key,
				Map.of(
						USER_ID, privateClaims.getUserId(),
						ROLE_TYPES, String.join(SEPARATOR, privateClaims.getRoleTypes())
				),
				expirySeconds
		);
	}

	public Optional<PrivateClaims> parse(String token) {
		return jwtHandler.parse(key, token).map(this::convert);
	}

	private PrivateClaims convert(Claims claims) {
		return new PrivateClaims(
				claims.get(USER_ID, String.class),
				Arrays.asList(claims.get(ROLE_TYPES, String.class).split(SEPARATOR))
		);
	}

	@Getter
	@AllArgsConstructor
	public static class PrivateClaims {
		private String userId;
		private List<String> roleTypes;
	}
}
