package com.y2gcoder.blog.controller.user;

import com.y2gcoder.blog.controller.response.ApiResponse;
import com.y2gcoder.blog.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "User Controller", tags = "User")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
	private final UserService userService;

	@ApiOperation(value = "사용자 정보 조회", notes = "사용자 정보를 조회한다.")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> findUser(@ApiParam(value = "사용자 ID", required = true) @PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success(userService.findUser(id)));
	}

	@ApiOperation(value = "사용자 정보 삭제", notes = "사용자 정보를 삭제한다.")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> delete(@ApiParam(value = "사용자 ID", required = true) @PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok(ApiResponse.success());
	}
}
