package com.y2gcoder.blog.repository.post;

import com.y2gcoder.blog.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
