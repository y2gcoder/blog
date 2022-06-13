package com.y2gcoder.blog.service.post.dto;

import com.y2gcoder.blog.repository.post.dto.PostSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
public class PostListDto {
	private boolean hasNext;
	private List<PostSimpleDto> content;

	public static PostListDto toDto(Slice<PostSimpleDto> slice) {
		return new PostListDto(slice.hasNext(), slice.getContent());
	}
}
