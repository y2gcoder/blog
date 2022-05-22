package com.y2gcoder.blog.controller.auth;

import com.y2gcoder.blog.controller.response.ApiResponse;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {
	private final AuthService authService;

	@PostMapping("/auth/sign-up")
	public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpRequest req) {
		authService.signUp(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success());
	}

	@PostMapping("/auth/sign-in")
	public ResponseEntity<ApiResponse> signIn(@Valid @RequestBody SignInRequest request) {
		return ResponseEntity.ok(ApiResponse.success(authService.signIn(request)));
	}
}
