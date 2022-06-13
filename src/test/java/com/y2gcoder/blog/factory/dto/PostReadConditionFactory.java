package com.y2gcoder.blog.factory.dto;

import com.y2gcoder.blog.repository.post.dto.PostReadCondition;

import java.util.Collections;
import java.util.List;

public class PostReadConditionFactory {
	public static PostReadCondition createPostReadCondition(Integer size, Long lastPostId) {
		return new PostReadCondition(size, lastPostId, Collections.emptyList());
	}

	public static PostReadCondition createPostReadCondition(Integer size, Long lastPostId, List<Long> categoryIds) {
		return new PostReadCondition(size, lastPostId, categoryIds);
	}
}
