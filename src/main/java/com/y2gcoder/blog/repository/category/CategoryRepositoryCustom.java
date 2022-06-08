package com.y2gcoder.blog.repository.category;

import com.y2gcoder.blog.entity.category.Category;

import java.util.List;

public interface CategoryRepositoryCustom {

	List<Category> findAllHierarchical();
}
