package com.y2gcoder.blog.factory.dto;

import com.y2gcoder.blog.service.post.dto.PostUpdateRequest;

public class PostUpdateRequestFactory {
	public static PostUpdateRequest createPostUpdateRequest(String title, String content, String thumbnailUrl) {
		return new PostUpdateRequest(title, content, thumbnailUrl);
	}
}
