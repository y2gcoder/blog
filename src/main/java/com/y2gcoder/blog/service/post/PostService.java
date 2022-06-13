package com.y2gcoder.blog.service.post;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.CategoryNotFoundException;
import com.y2gcoder.blog.exception.PostNotFoundException;
import com.y2gcoder.blog.exception.UserNotFoundException;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.post.PostRepository;
import com.y2gcoder.blog.repository.post.dto.PostReadCondition;
import com.y2gcoder.blog.repository.user.UserRepository;
import com.y2gcoder.blog.service.post.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	@Transactional
	public PostCreateResponse create(PostCreateRequest req) {
		User user = userRepository.findById(req.getUserId()).orElseThrow(UserNotFoundException::new);
		Category category = categoryRepository.findById(req.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
		Post post = postRepository.save(new Post(req.getTitle(), req.getContent(), req.getThumbnailUrl(), user, category));
		return new PostCreateResponse(post.getId());
	}

	public PostDto read(Long id) {
		return PostDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
	}

	@Transactional
	@PreAuthorize("@postGuard.check(#id)")
	public void delete(Long id) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		postRepository.delete(post);
	}

	@Transactional
	@PreAuthorize("@postGuard.check(#id)")
	public PostUpdateResponse update(Long id, PostUpdateRequest req) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		post.update(req.getTitle(), req.getContent(), req.getThumbnailUrl());
		return new PostUpdateResponse(post.getId());
	}

	public PostListDto readAll(PostReadCondition condition) {
		return PostListDto.toDto(postRepository.findAllByCondition(condition));
	}
}
