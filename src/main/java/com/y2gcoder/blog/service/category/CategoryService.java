package com.y2gcoder.blog.service.category;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.exception.CategoryNotFoundException;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public List<CategoryDto> findAllHierarchical() {
		List<Category> categories = categoryRepository.findAllHierarchical();
		return categories.stream().map(CategoryDto::new).collect(Collectors.toList());
	}

	@Transactional
	public void create(CategoryCreateRequest req) {
		if (existsParentId(req)) {
			Category parentCategory = categoryRepository
					.findById(req.getParentId())
					.orElseThrow(CategoryNotFoundException::new);
			Category category = new Category(req.getName(), parentCategory);
			categoryRepository.save(category);
		} else {
			Category category = new Category(req.getName(), null);
			categoryRepository.save(category);
		}
	}

	private boolean existsParentId(CategoryCreateRequest req) {
		return req.getParentId() != null;
	}

	@Transactional
	public void delete(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
		categoryRepository.delete(category);
	}

}
