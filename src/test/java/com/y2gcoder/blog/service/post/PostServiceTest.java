package com.y2gcoder.blog.service.post;

import com.y2gcoder.blog.exception.CategoryNotFoundException;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.post.PostRepository;
import com.y2gcoder.blog.repository.user.UserRepository;
import com.y2gcoder.blog.service.post.dto.PostCreateRequest;
import com.y2gcoder.blog.service.post.dto.PostCreateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.y2gcoder.blog.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static com.y2gcoder.blog.factory.entity.CategoryFactory.createCategory;
import static com.y2gcoder.blog.factory.entity.PostFactory.createPost;
import static com.y2gcoder.blog.factory.entity.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
	@InjectMocks PostService postService;
	@Mock
	PostRepository postRepository;
	@Mock
	UserRepository userRepository;
	@Mock
	CategoryRepository categoryRepository;

	@Test
	void createTest() {
		//given
		PostCreateRequest req = createPostCreateRequest();
		given(userRepository.findById(anyLong())).willReturn(Optional.of(createUser()));
		given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));
		given(postRepository.save(any())).willReturn(createPost());

		//when
		postService.create(req);

		//then
		verify(postRepository).save(any());
	}

	@Test
	void createExceptionByUserNotFoundTest() {
		//given
		given(userRepository.findById(anyLong())).willReturn(Optional.empty());

		//when, then
		assertThatThrownBy(() -> postService.create(createPostCreateRequest()))
				.isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void createExceptionByCategoryNotFoundTest() {
		//given
		given(userRepository.findById(anyLong())).willReturn(Optional.of(createUser()));
		given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

		//when, then
		assertThatThrownBy(() -> postService.create(createPostCreateRequest()))
				.isInstanceOf(CategoryNotFoundException.class);
	}

}