package com.y2gcoder.blog.service.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponse {
	private String accessToken;
	private String refreshToken;
}
