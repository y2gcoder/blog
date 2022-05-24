package com.y2gcoder.blog.handler;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtHandler {
	private final String TYPE = "Bearer ";

	public String createToken(String key, Map<String, Object> privateClaims, long expirySeconds) {
		Date now = new Date();
		return TYPE + Jwts.builder()
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + expirySeconds * 1000L))
				.addClaims(privateClaims)
				.signWith(SignatureAlgorithm.HS256, key.getBytes())
				.compact();
	}

	public Optional<Claims> parse(String key, String token) {
		try {
			return Optional.of(Jwts.parser().setSigningKey(key.getBytes()).parseClaimsJws(untype(token)).getBody());
		} catch (JwtException e) {
			return Optional.empty();
		}
	}

	private String untype(String token) {
		return token.substring(TYPE.length());
	}
}
