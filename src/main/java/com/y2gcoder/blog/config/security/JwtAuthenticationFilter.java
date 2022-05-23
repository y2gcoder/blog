package com.y2gcoder.blog.config.security;

import com.y2gcoder.blog.config.token.TokenHelper;
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
	private final TokenHelper accessTokenHelper;
	private final CustomUserDetailService userDetailService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = extractToken(request);
		if (validateToken(token)) {
			setAuthentication(token);
		}

		chain.doFilter(request, response);
	}

	private String extractToken(ServletRequest request) {
		return ((HttpServletRequest) request).getHeader("Authorization");
	}

	private boolean validateToken(String token) {
		return token != null && accessTokenHelper.validate(token);
	}

	private void setAuthentication(String token) {
		String userId = accessTokenHelper.extractSubject(token);
		CustomUserDetails userDetails = userDetailService.loadUserByUsername(userId);
		SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
	}
}
