package com.y2gcoder.blog.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {
	private boolean success;
	private int code;
	private ApiResult result;

	public static ApiResponse success() {
		return new ApiResponse(true, 0, null);
	}

	public static <T> ApiResponse success(T data) {
		return new ApiResponse(true, 0, new ApiSuccess<>(data));
	}

	public static ApiResponse failure(int code, String msg) {
		return new ApiResponse(false, code, new ApiFailure(msg));
	}
}
