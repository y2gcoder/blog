package com.y2gcoder.blog.service.post.dto;

import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.post.Post;
import com.y2gcoder.blog.entity.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

@ApiModel(value = "게시글 생성 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

	@ApiModelProperty(value = "포스트 제목", notes = "포스트 제목을 입력해주세요.", required = true, example = "my title")
	@NotBlank(message = "{postCreateRequest.title.notBlank}")
	private String title;

	@ApiModelProperty(value = "포스트 본문", notes = "포스트 본문을 입력해주세요.", required = true, example = "my content")
	@NotBlank(message = "{postCreateRequest.content.notBlank}")
	private String content;

	@ApiModelProperty(hidden = true)
	@Null
	private Long userId;

	@ApiModelProperty(value = "카테고리 아이디", notes = "카테고리 아이디를 입력해주세요.", required = true, example = "3")
	@NotNull(message = "{postCreateRequest.categoryId.notNull}")
	@PositiveOrZero(message = "{postCreateRequest.categoryId.positiveOrZero}")
	private Long categoryId;

	@ApiModelProperty(value = "썸네일 URL", notes = "썸네일 URL을 입력해주세요.")
	@URL(message = "{postCreateRequest.thumbnailUrl.url}")
	private String thumbnailUrl;

	public static Post toEntity(PostCreateRequest req, User user, Category category) {
		return new Post(req.title, req.content, req.thumbnailUrl, user, category);
	}
}
