package com.y2gcoder.blog.repository.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReadCondition {
	@NotNull(message = "{postReadCondition.size.notNull}")
	@Positive(message = "{postReadCondition.size.positive}")
	private Integer size;

	@Positive(message = "{postReadCondition.lastPostId.positive}")
	private Long lastPostId;

	private List<Long> categoryIds = new ArrayList<>();
}
