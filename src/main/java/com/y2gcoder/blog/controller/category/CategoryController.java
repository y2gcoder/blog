package com.y2gcoder.blog.controller.category;

import com.y2gcoder.blog.controller.response.ApiResponse;
import com.y2gcoder.blog.service.category.CategoryCreateRequest;
import com.y2gcoder.blog.service.category.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Category Controller", tags = "Category")
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@RestController
public class CategoryController {
	private final CategoryService categoryService;

	@ApiOperation(value = "모든 카테고리 조회", notes = "모든 카테고리를 조회한다.")
	@GetMapping
	public ResponseEntity<ApiResponse> findAll() {
		return ResponseEntity.ok(ApiResponse.success(categoryService.findAllHierarchical()));
	}

	@ApiOperation(value = "카테고리 생성", notes = "카테고리를 생성한다.")
	@PostMapping
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody CategoryCreateRequest req) {
		categoryService.create(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success());
	}

	@ApiOperation(value = "카테고리 삭제", notes = "카테고리를 삭제한다.")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> delete(@ApiParam(value = "카테고리 id", required = true) @PathVariable Long id) {
		categoryService.delete(id);
		return ResponseEntity.ok(ApiResponse.success());
	}
}
