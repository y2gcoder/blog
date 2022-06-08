package com.y2gcoder.blog.factory.entity;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.entity.user.User;

import static com.y2gcoder.blog.factory.entity.CategoryFactory.createCategory;
import static com.y2gcoder.blog.factory.entity.UserFactory.createUser;

public class PostFactory {
	public static Post createPost() {
		return createPost(createUser(), createCategory());
	}

	public static Post createPost(User user, Category category) {
		return new Post("title", "content", "", user, category);
	}

	public static Post createPostWithThumbnailUrl(String thumbnailUrl) {
		return new Post("title", "content", thumbnailUrl, createUser(), createCategory());
	}

	public static Post createPostWithThumbnailUrl(User user, Category category, String thumbnailUrl) {
		return new Post("title", "content", thumbnailUrl, user, category);
	}
}
