package com.y2gcoder.blog.factory.dto;

import com.y2gcoder.blog.service.post.dto.PostCreateRequest;

public class PostCreateRequestFactory {
	public static PostCreateRequest createPostCreateRequest() {
		return new PostCreateRequest("title", "content", 1L, 1L, "");
	}

	public static PostCreateRequest createPostCreateRequest(
			String title, String content, Long userId, Long categoryId, String thumbnailUrl
	) {
		return new PostCreateRequest(title, content, userId, categoryId, thumbnailUrl);
	}

	public static PostCreateRequest createPostCreateRequestWithTitle(String title) {
		return new PostCreateRequest(title, "content", 1L, 1L, "");
	}

	public static PostCreateRequest createPostCreateRequestWithContent(String content) {
		return new PostCreateRequest("title", content, 1L, 1L, "");
	}

	public static PostCreateRequest createPostCreateRequestWithUserId(Long userId) {
		return new PostCreateRequest("title", "content", userId, 1L, "");
	}

	public static PostCreateRequest createPostCreateRequestWithCategoryId(Long categoryId) {
		return new PostCreateRequest("title", "content", 1L, categoryId, "");
	}

	public static PostCreateRequest createPostCreateRequestWithThumbnailUrl(String thumbnailUrl) {
		return new PostCreateRequest("title", "content", 1L, 1L, thumbnailUrl);
	}
}
