package com.y2gcoder.blog.entity.post;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.common.BaseTimeEntity;
import com.y2gcoder.blog.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String title;

	@Lob
	@Column(nullable = false)
	private String content;

	@Column(nullable = false, length = 1000)
	private String thumbnailUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	public Post(String title, String content, String thumbnailUrl, User user, Category category) {
		this.title = title;
		this.content = content;
		this.thumbnailUrl = makeDefaultThumbnail(thumbnailUrl);
		this.user = user;
		this.category = category;
	}

	private String makeDefaultThumbnail(String thumbnailUrl) {
		String defaultThumbnailUrl = "https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80";
		if (thumbnailUrl == null || thumbnailUrl.equals("")) {
			thumbnailUrl = defaultThumbnailUrl;
		}
		return thumbnailUrl;
	}

	public void update(String title, String content, String thumbnailUrl) {
		if (title != null && !title.equals("")) {
			this.title = title;
		}
		if (content != null && !content.equals("")) {
			this.content = content;
		}
		this.thumbnailUrl = thumbnailUrl;
	}

}
