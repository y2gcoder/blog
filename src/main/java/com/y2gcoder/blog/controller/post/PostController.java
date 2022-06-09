package com.y2gcoder.blog.controller.post;

import com.y2gcoder.blog.annotation.AssignUserId;
import com.y2gcoder.blog.controller.response.ApiResponse;
import com.y2gcoder.blog.service.post.PostService;
import com.y2gcoder.blog.service.post.dto.PostCreateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value = "Post Controller", tags = "Post")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {
	private final PostService postService;

	@ApiOperation(value = "포스트 생성", notes = "포스트를 생성한다.")
	@AssignUserId
	@PostMapping
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody PostCreateRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(postService.create(req)));
	}
}
