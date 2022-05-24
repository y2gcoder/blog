package com.y2gcoder.blog.config.token;

import com.y2gcoder.blog.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenHelperTest {
	TokenHelper tokenHelper;

	@BeforeEach
	void beforeEach() {
		tokenHelper = new TokenHelper(new JwtHandler(), "myKey", 1000L);
	}

	@Test
	void createTokenAndParseTest() {
		//given
		String userId = "1";
		List<String> roleTypes = List.of("USER", "ADMIN");
		TokenHelper.PrivateClaims privateClaims = new TokenHelper.PrivateClaims(userId, roleTypes);

		//when
		String token = tokenHelper.createToken(privateClaims);

		//then
		TokenHelper.PrivateClaims parsedPrivateClaims = tokenHelper.parse(token).orElseThrow(RuntimeException::new);
		assertThat(parsedPrivateClaims.getUserId()).isEqualTo(userId);
		assertThat(parsedPrivateClaims.getRoleTypes()).contains(roleTypes.get(0), roleTypes.get(1));
	}
}