package com.y2gcoder.blog.repository.post;

import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.repository.post.dto.PostReadCondition;
import com.y2gcoder.blog.repository.post.dto.PostSimpleDto;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface PostRepositoryCustom {
	Optional<Post> findByIdWithUser(Long id);

	Slice<PostSimpleDto> findAllByCondition(PostReadCondition condition);
}
