package com.y2gcoder.blog.repository.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.category.QCategory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.y2gcoder.blog.entity.category.QCategory.category;

public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public CategoryRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}


	@Override
	public List<Category> findAllHierarchical() {
		QCategory parent = new QCategory("parent");
		QCategory child = new QCategory("child");

		return queryFactory.selectFrom(parent)
				.distinct()
				.leftJoin(parent.children, child)
				.fetchJoin()
				.where(
						parent.parent.isNull()
				)
				.orderBy(parent.sortOrder.asc(), child.sortOrder.asc())
				.fetch();

	}
}
