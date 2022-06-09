package com.y2gcoder.blog.service.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.service.category.CategoryDto;
import com.y2gcoder.blog.service.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostDto {
	private Long id;
	private String title;
	private String content;
	private String thumbnailUrl;
	private UserDto user;
	private PostCategory category;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime updatedAt;

	public static PostDto toDto(Post post) {
		return new PostDto(
				post.getId(),
				post.getTitle(),
				post.getContent(),
				post.getThumbnailUrl(),
				new UserDto(post.getUser()),
				new PostCategory(post.getCategory()),
				post.getCreatedAt(),
				post.getUpdatedAt()
		);
	}

}
