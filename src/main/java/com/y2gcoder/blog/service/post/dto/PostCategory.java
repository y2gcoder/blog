package com.y2gcoder.blog.service.post.dto;

import com.y2gcoder.blog.entity.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCategory {
	private Long id;
	private String name;

	public PostCategory(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}
}
