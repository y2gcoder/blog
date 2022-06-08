package com.y2gcoder.blog.factory.dto;

import com.y2gcoder.blog.service.category.CategoryCreateRequest;

public class CategoryCreateRequestFactory {
	public static CategoryCreateRequest createCategoryCreateRequest() {
		return new CategoryCreateRequest("category", null);
	}

	public static CategoryCreateRequest createCategoryCreateRequestWithName(String name) {
		return new CategoryCreateRequest(name, null);
	}
}
