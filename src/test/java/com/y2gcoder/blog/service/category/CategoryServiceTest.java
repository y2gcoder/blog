package com.y2gcoder.blog.service.category;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.exception.CategoryNotFoundException;
import com.y2gcoder.blog.factory.dto.CategoryCreateRequestFactory;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.y2gcoder.blog.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
	@InjectMocks CategoryService categoryService;
	@Mock CategoryRepository categoryRepository;

	@Test
	void findAllHierarchicalTest() {
		//given
		given(categoryRepository.findAllHierarchical())
				.willReturn(
						List.of(Category.createCategory("name1"), Category.createCategory("name2", 1))
				);

		//when
		List<CategoryDto> result = categoryService.findAllHierarchical();

		//then
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).getName()).isEqualTo("name1");
		assertThat(result.get(1).getName()).isEqualTo("name2");
	}

	@Test
	void createTest() {
		//given
		CategoryCreateRequest req = createCategoryCreateRequest();

		//when
		categoryService.create(req);

		//then
		verify(categoryRepository).save(any());
	}

	@Test
	void deleteTest() {
		//given
		given(
				categoryRepository.findById(anyLong())
		).willReturn(Optional.of(Category.createCategory("category")));

		//when
		categoryService.delete(1L);

		//then
		verify(categoryRepository).delete(any());
	}

	@Test
	void deleteExceptionByCategoryNotFoundTest() {
		//given
		given(
				categoryRepository.findById(anyLong())
		).willReturn(Optional.empty());

		//when, then
		assertThatThrownBy(() -> categoryService.delete(1L)).isInstanceOf(CategoryNotFoundException.class);
	}
}