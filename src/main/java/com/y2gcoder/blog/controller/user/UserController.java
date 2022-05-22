package com.y2gcoder.blog.controller.user;

import com.y2gcoder.blog.controller.response.ApiResponse;
import com.y2gcoder.blog.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
	private final UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> findUser(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success(userService.findUser(id)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok(ApiResponse.success());
	}
}
