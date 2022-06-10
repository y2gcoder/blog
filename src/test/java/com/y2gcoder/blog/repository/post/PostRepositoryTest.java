package com.y2gcoder.blog.repository.post;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.entity.user.User;
import com.y2gcoder.blog.exception.PostNotFoundException;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.user.UserRepository;
import com.y2gcoder.blog.service.post.dto.PostUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.y2gcoder.blog.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static com.y2gcoder.blog.factory.entity.CategoryFactory.createCategory;
import static com.y2gcoder.blog.factory.entity.PostFactory.createPost;
import static com.y2gcoder.blog.factory.entity.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {
	@Autowired
	PostRepository postRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@PersistenceContext
	EntityManager em;

	User user;
	Category category;

	@BeforeEach
	void beforeEach() {
		user = userRepository.save(createUser());
		category = categoryRepository.save(createCategory());
	}

	@Test
	void createAndReadTest() {
		//given
		Post post = postRepository.save(createPost(user, category));
		flushAndClear();

		//when
		Post foundPost = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);

		//then
		assertThat(foundPost.getId()).isEqualTo(post.getId());
		assertThat(foundPost.getTitle()).isEqualTo(post.getTitle());
	}

	@Test
	void deleteTest() {
		//given
		Post post = postRepository.save(createPost(user, category));
		flushAndClear();

		//when
		postRepository.delete(post);
		flushAndClear();

		//then
		assertThatThrownBy(() -> postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new))
				.isInstanceOf(PostNotFoundException.class);
	}

	@Test
	void findByIdWithUserTest() {
		//given
		Post post = postRepository.save(createPost(user, category));

		//when
		Post foundPost = postRepository.findByIdWithUser(post.getId()).orElseThrow(PostNotFoundException::new);

		//then
		User foundUser = foundPost.getUser();
		assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
	}

	@Test
	void updateTest() {
		//given
		Post post = postRepository.save(createPost(user, category));
		flushAndClear();

		//when
		PostUpdateRequest req = createPostUpdateRequest(
				"Updated Title",
				"Updated Content",
				"https://images.unsplash.com/photo-1516914357598-8c2b86ae72b6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
		);
		Post foundPost = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
		foundPost.update(req.getTitle(), req.getContent(), req.getThumbnailUrl());
		flushAndClear();

		//then
		Post result = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
		assertThat(result.getTitle()).isEqualTo(req.getTitle());
		assertThat(result.getContent()).isEqualTo(req.getContent());
		assertThat(result.getThumbnailUrl()).isEqualTo(req.getThumbnailUrl());
	}

	private void flushAndClear() {
		em.flush();
		em.clear();
	}








}