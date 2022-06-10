package com.y2gcoder.blog.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final CustomUserDetailService userDetailService;


	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.mvcMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/auth/sign-up", "/api/auth/sign-in", "/api/auth/refresh-token").permitAll()
				.antMatchers(HttpMethod.DELETE, "/api/users/{id}/**").authenticated()
				.antMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/api/posts").authenticated()
				.antMatchers(HttpMethod.DELETE, "/api/posts/{id}").authenticated()
				.antMatchers(HttpMethod.PUT, "/api/posts/{id}").authenticated()
				.antMatchers(HttpMethod.GET, "/api/**").permitAll()
				.anyRequest().hasAnyRole("ADMIN")
				.and()
				.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
				.and()
				.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(userDetailService), UsernamePasswordAuthenticationFilter.class);

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}