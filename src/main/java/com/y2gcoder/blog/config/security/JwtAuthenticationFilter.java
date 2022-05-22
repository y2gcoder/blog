package com.y2gcoder.blog.config.security;

import com.y2gcoder.blog.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtService jwtService;
	private final CustomUserDetailService userDetailService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = extractToken(request);
		if (validateAccessToken(token)) {
			setAccessAuthentication("access", token);
		} else if (validateRefreshToken(token)) {
			setRefreshAuthentication("refresh", token);
		}

		chain.doFilter(request, response);
	}

	private String extractToken(ServletRequest request) {
		return ((HttpServletRequest) request).getHeader("Authorization");
	}

	private boolean validateAccessToken(String token) {
		return token != null && jwtService.validateAccessToken(token);
	}

	private boolean validateRefreshToken(String token) {
		return token != null && jwtService.validateRefreshToken(token);
	}

	private void setAccessAuthentication(String type, String token) {
		String userId = jwtService.extractAccessTokenSubject(token);
		CustomUserDetails userDetails = userDetailService.loadUserByUsername(userId);
		SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(type, userDetails, userDetails.getAuthorities()));
	}

	private void setRefreshAuthentication(String type, String token) {
		String userId = jwtService.extractRefreshTokenSubject(token);
		CustomUserDetails userDetails = userDetailService.loadUserByUsername(userId);
		SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(type, userDetails, userDetails.getAuthorities()));
	}
}
