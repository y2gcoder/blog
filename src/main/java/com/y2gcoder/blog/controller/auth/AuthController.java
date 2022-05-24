package com.y2gcoder.blog.controller.auth;

import com.y2gcoder.blog.controller.response.ApiResponse;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(value = "Auth Controller", tags = "Auth")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
	private final AuthService authService;

	@ApiOperation(value = "회원가입", notes = "블로그에 회원가입을 한다.")
	@PostMapping("/sign-up")
	public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpRequest req) {
		authService.signUp(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success());
	}

	@ApiOperation(value = "로그인", notes = "블로그에 로그인한다.")
	@PostMapping("/sign-in")
	public ResponseEntity<ApiResponse> signIn(@Valid @RequestBody SignInRequest request) {
		return ResponseEntity.ok(ApiResponse.success(authService.signIn(request)));
	}

	@ApiOperation(value = "토큰 재발급", notes = "리프레시 토큰으로 액세스 토큰을 재발급한다.")
	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponse> refreshToken(@ApiIgnore @RequestHeader(value = "Authorization") String refreshToken) {
		return ResponseEntity.ok(ApiResponse.success(authService.refreshToken(refreshToken)));
	}
}
