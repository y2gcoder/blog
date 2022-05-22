package com.y2gcoder.blog.factory.dto;

import com.y2gcoder.blog.service.auth.dto.SignInResponse;

public class SignInResponseFactory {
	public static SignInResponse createSignInResponse(String accessToken, String refreshToken) {
		return new SignInResponse(accessToken, refreshToken);
	}
}
