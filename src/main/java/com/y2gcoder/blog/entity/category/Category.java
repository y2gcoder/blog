package com.y2gcoder.blog.entity.category;

import com.y2gcoder.blog.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private int depth;

	@Column(nullable = false)
	private int sortOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Category parent;

	@OneToMany(mappedBy = "parent")
	private List<Category> children = new ArrayList<>();

	@Builder
	public Category(String name, int depth, int sortOrder, Category parent) {
		this.name = name;
		this.depth = depth;
		this.sortOrder = sortOrder;
		this.parent = parent;
	}

	public static Category createCategory(String name) {
		return Category.builder()
				.name(name)
				.build();
	}

	public static Category createCategory(String name, int sortOrder) {
		return Category.builder()
				.name(name)
				.sortOrder(sortOrder)
				.build();
	}

	public static Category createSubCategory(String name, Category parent) {
		int sortOrder = parent.getChildren().isEmpty()
				? 0
				: parent.getChildren().get(parent.getChildren().size() - 1).getSortOrder() + 1;
		return Category.builder()
				.name(name)
				.parent(parent)
				.depth(parent.getDepth() + 1)
				.sortOrder(sortOrder)
				.build();
	}
}
