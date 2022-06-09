package com.y2gcoder.blog.repository.post;

import com.y2gcoder.blog.entity.post.Post;

import java.util.Optional;

public interface PostRepositoryCustom {
	Optional<Post> findByIdWithUser(Long id);
}
