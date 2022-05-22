package com.y2gcoder.blog.factory.dto;

import com.y2gcoder.blog.service.auth.dto.SignInRequest;

public class SignInRequestFactory {
	public static SignInRequest createSignInRequest() {
		return new SignInRequest("email@email.com", "1q2w3e4r!");
	}

	public static SignInRequest createSignInRequest(String email, String password) {
		return new SignInRequest(email, password);
	}

	public static SignInRequest createSignInRequestWithEmail(String email) {
		return new SignInRequest(email, "1q2w3e4r!");
	}

	public static SignInRequest createSignInRequestWithPassword(String password) {
		return new SignInRequest("email@email.com", password);
	}
}
