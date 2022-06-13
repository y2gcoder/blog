package com.y2gcoder.blog.service.post.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "포스트 수정 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {
	@ApiModelProperty(value = "포스트 제목", notes = "포스트 제목을 입력해주세요.", required = true, example = "my title")
	@NotBlank(message = "{postUpdateRequest.title.notBlank}")
	private String title;

	@ApiModelProperty(value = "포스트 본문", notes = "포스트 본문을 입력해주세요.", required = true, example = "my content")
	@NotBlank(message = "{postUpdateRequest.content.NotBlank}")
	private String content;

	@ApiModelProperty(value = "썸네일 URL", notes = "썸네일 URL을 입력해주세요.")
	@URL(message = "{postUpdateRequest.thumbnailUrl.url}")
	private String thumbnailUrl;
}
