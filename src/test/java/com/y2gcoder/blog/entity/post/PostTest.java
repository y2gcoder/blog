package com.y2gcoder.blog.entity.post;

import com.y2gcoder.blog.service.post.dto.PostUpdateRequest;
import org.junit.jupiter.api.Test;

import static com.y2gcoder.blog.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static com.y2gcoder.blog.factory.entity.CategoryFactory.createCategory;
import static com.y2gcoder.blog.factory.entity.PostFactory.createPost;
import static com.y2gcoder.blog.factory.entity.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {
	@Test
	void updateTest() {
		//given
		Post post = createPost(createUser(), createCategory());

		//when
		PostUpdateRequest req = createPostUpdateRequest(
				"Updated Title",
				"Updated Content",
				"https://images.unsplash.com/photo-1516914357598-8c2b86ae72b6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
		);
		post.update(req.getTitle(), req.getContent(), req.getThumbnailUrl());

		//then
		assertThat(post.getTitle()).isEqualTo(req.getTitle());
		assertThat(post.getContent()).isEqualTo(req.getContent());
		assertThat(post.getThumbnailUrl()).isEqualTo(req.getThumbnailUrl());
	}
}