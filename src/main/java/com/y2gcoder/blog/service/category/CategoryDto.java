package com.y2gcoder.blog.service.category;

import com.y2gcoder.blog.entity.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
	private Long id;
	private String name;
	private List<CategoryDto> children;

	public CategoryDto(Category category) {
		this.id = category.getId();
		this.name = category.getName();
		this.children = category.getChildren().stream().map(CategoryDto::new)
				.collect(Collectors.toList());
	}
}
