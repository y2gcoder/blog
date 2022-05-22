package com.y2gcoder.blog.handler;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHandler {
	private String type = "Bearer ";

	public String createToken(String encodedKey, String subject, long expirySeconds) {
		Date now = new Date();
		return type + Jwts.builder()
				.setSubject(subject)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + expirySeconds * 1000L))
				.signWith(SignatureAlgorithm.HS256, encodedKey)
				.compact();
	}

	public String extractSubject(String encodedKey, String token) {
		return parse(encodedKey, token).getBody().getSubject();
	}

	public boolean validate(String encodedKey, String token) {
		try {
			parse(encodedKey, token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	private Jws<Claims> parse(String key, String token) {
		return Jwts.parser()
				.setSigningKey(key)
				.parseClaimsJws(untype(token));
	}

	private String untype(String token) {
		return token.substring(type.length());
	}
}
