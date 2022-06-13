package com.y2gcoder.blog.repository.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.y2gcoder.blog.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSimpleDto {
	private Long id;
	private String title;
	private String thumbnailUrl;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	public PostSimpleDto(Post post) {
		this.id = post.getId();
		this.title = post.getTitle();
		this.thumbnailUrl = post.getThumbnailUrl();
		this.createdAt = post.getCreatedAt();
	}
}
