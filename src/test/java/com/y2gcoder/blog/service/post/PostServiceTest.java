package com.y2gcoder.blog.service.post;

import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.exception.CategoryNotFoundException;
import com.y2gcoder.blog.exception.PostNotFoundException;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.post.PostRepository;
import com.y2gcoder.blog.repository.post.dto.PostSimpleDto;
import com.y2gcoder.blog.repository.user.UserRepository;
import com.y2gcoder.blog.service.post.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Collections;
import java.util.Optional;

import static com.y2gcoder.blog.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static com.y2gcoder.blog.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static com.y2gcoder.blog.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static com.y2gcoder.blog.factory.entity.CategoryFactory.createCategory;
import static com.y2gcoder.blog.factory.entity.PostFactory.createPost;
import static com.y2gcoder.blog.factory.entity.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

	@Test
	void readTest() {
		//given
		Post post = createPost();
		given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

		//when
		PostDto postDto = postService.read(1L);

		//then
		assertThat(postDto.getTitle()).isEqualTo(post.getTitle());
	}

	@Test
	void readExceptionByPostNotFoundTest() {
		//given
		given(postRepository.findById(anyLong())).willReturn(Optional.empty());
		//when, then
		assertThatThrownBy(() -> postService.read(1L)).isInstanceOf(PostNotFoundException.class);
	}

	@Test
	void deleteTest() {
		//given
		Post post = createPost();
		given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

		//when
		postService.delete(1L);

		//then
		verify(postRepository).delete(any());
	}

	@Test
	void updateTest() {
		//given
		Post post = createPost();
		given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
		PostUpdateRequest req = createPostUpdateRequest(
				"Updated Title",
				"Updated Content",
				"https://images.unsplash.com/photo-1516914357598-8c2b86ae72b6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
		);

		//when
		PostUpdateResponse result = postService.update(1L, req);

		//then
		//TODO 어떻게 검증할 것인지 고민
		assertThat(result.getId()).isEqualTo(post.getId());

	}

	@Test
	void updateExceptionByPostNotFoundTest() {
		//given
		given(postRepository.findById(anyLong())).willReturn(Optional.empty());

		//then
		assertThatThrownBy(
				() -> postService.update(1L, createPostUpdateRequest("title", "content", ""))
		)
				.isInstanceOf(PostNotFoundException.class);
	}

	@Test
	void readAllTest() {
		//given
		given(postRepository.findAllByCondition(any())).willReturn(new SliceImpl<>(Collections.emptyList()));

		//when
		PostListDto postListDto = postService.readAll(createPostReadCondition(1, null));

		//then
		assertThat(postListDto.getContent().size()).isZero();
	}
}