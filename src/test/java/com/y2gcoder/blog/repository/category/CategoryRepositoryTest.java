package com.y2gcoder.blog.repository.category;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.exception.CategoryNotFoundException;
import com.y2gcoder.blog.factory.entity.CategoryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.y2gcoder.blog.factory.entity.CategoryFactory.createCategory;
import static com.y2gcoder.blog.factory.entity.CategoryFactory.createCategoryWithName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {
	@Autowired CategoryRepository categoryRepository;
	@PersistenceContext
	EntityManager entityManager;

	@Test
	void createAndReadTest() {
		//given
		Category category = createCategory();

		//when
		Category savedCategory = categoryRepository.save(category);
		flushAndClear();
		//then
		Category foundCategory = categoryRepository.findById(savedCategory.getId())
				.orElseThrow(CategoryNotFoundException::new);
		assertThat(foundCategory.getId()).isEqualTo(savedCategory.getId());
	}

	@Test
	void readAllTest() {
		//given
		List<Category> categories = Stream.of("category1", "category2", "category3")
				.map(CategoryFactory::createCategoryWithName).collect(Collectors.toList());
		categoryRepository.saveAll(categories);
		flushAndClear();
		//when
		List<Category> foundCategories = categoryRepository.findAll();
		//then
		assertThat(foundCategories.size()).isEqualTo(3);

	}

	@Test
	void deleteTest() {
		//given
		Category category = createCategoryWithName("category1");
		categoryRepository.save(category);
		flushAndClear();

		//when
		categoryRepository.delete(category);
		flushAndClear();

		//then
		assertThatThrownBy(
				() -> categoryRepository.findById(category.getId())
						.orElseThrow(CategoryNotFoundException::new)
		).isInstanceOf(CategoryNotFoundException.class);

	}

	@Test
	void deleteCascadeTest() {
		//given
		Category category1 = categoryRepository.save(createCategoryWithName("category1"));
		Category category1_1 = categoryRepository.save(createCategory("category1-1", category1));
		Category category1_1_1 = categoryRepository.save(createCategory("category1-1-1", category1_1));
		Category category2 = categoryRepository.save(createCategoryWithName("category2"));
		flushAndClear();

		//when
		categoryRepository.delete(category1);
		flushAndClear();

		//then
		List<Category> result = categoryRepository.findAll();
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).getId()).isEqualTo(category2.getId());
	}

	@Test
	void findAllHierarchicalTest() {
		//given
		Category category1 = categoryRepository.save(createCategoryWithName("category1"));
		Category category1_1 = categoryRepository.save(createCategory("category1-1", category1));
		Category category1_1_1 = categoryRepository.save(createCategory("category1-1-1", category1_1));
		Category category1_1_2 = categoryRepository.save(createCategory("category1-1-2", category1_1));
		Category category2 = categoryRepository.save(createCategoryWithName("category2"));
		flushAndClear();

		//when
		List<Category> result = categoryRepository.findAllHierarchical();

		//then
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).getChildren().size()).isEqualTo(1);
		assertThat(result.get(0).getChildren().get(0).getChildren().size()).isEqualTo(2);
		assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getId()).isEqualTo(category1_1_1.getId());
	}

	private void flushAndClear() {
		entityManager.flush();
		entityManager.clear();
	}

}