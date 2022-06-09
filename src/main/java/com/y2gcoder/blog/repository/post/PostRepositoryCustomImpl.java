package com.y2gcoder.blog.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.entity.post.QPost;
import com.y2gcoder.blog.entity.user.QUser;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.y2gcoder.blog.entity.post.QPost.post;
import static com.y2gcoder.blog.entity.user.QUser.user;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public PostRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Optional<Post> findByIdWithUser(Long id) {
		return queryFactory
				.selectFrom(post)
				.join(post.user, user)
				.fetchJoin()
				.where(post.id.eq(id))
				.stream().findAny();

	}
}
