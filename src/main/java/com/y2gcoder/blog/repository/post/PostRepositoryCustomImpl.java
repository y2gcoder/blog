package com.y2gcoder.blog.repository.post;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.y2gcoder.blog.entity.category.QCategory;
import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.entity.post.QPost;
import com.y2gcoder.blog.entity.user.QUser;
import com.y2gcoder.blog.repository.post.dto.PostReadCondition;
import com.y2gcoder.blog.repository.post.dto.PostSimpleDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.y2gcoder.blog.entity.category.QCategory.category;
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

	@Override
	public Slice<PostSimpleDto> findAllByCondition(PostReadCondition condition) {
		PageRequest pageRequest = PageRequest.of(0, condition.getSize());
		Long lastPostId = condition.getLastPostId();
		List<Long> categoryIds = condition.getCategoryIds();
		List<Post> result = queryFactory.selectFrom(post)
				.join(post.category, category)
				.where(getNullOrInCategoryIds(categoryIds), getWhereIdLt(lastPostId))
				.orderBy(post.id.desc())
				.limit(pageRequest.getPageSize() + 1)
				.fetch();

		List<PostSimpleDto> content = result.stream()
				.map(PostSimpleDto::new).collect(Collectors.toList());

		boolean hasNext = false;
		if (content.size() > pageRequest.getPageSize()) {
			content.remove(pageRequest.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(content, pageRequest, hasNext);
	}

	private Predicate getNullOrInCategoryIds(List<Long> categoryIds) {
		return categoryIds.isEmpty() ? null : post.category.id.in(categoryIds);
	}

	private Predicate getWhereIdLt(Long lastPostId) {
		return lastPostId == null ? null : post.id.lt(lastPostId);
	}


}
