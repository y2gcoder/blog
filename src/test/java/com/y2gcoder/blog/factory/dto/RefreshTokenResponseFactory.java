package com.y2gcoder.blog.factory.dto;

import com.y2gcoder.blog.service.auth.dto.RefreshTokenResponse;

public class RefreshTokenResponseFactory {
	public static RefreshTokenResponse createRefreshTokenResponse(String accessToken) {
		return new RefreshTokenResponse(accessToken);
	}
}
