package roomescape.member;

import static org.assertj.core.api.Assertions.*;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.member.dto.MemberResponse;
import roomescape.member.enums.Role;

class JwtProviderTest {

	private static final String TEST_SECRET_KEY = "testtesttesttesttesttesttesttesttesttesttesttest";

	private JwtProvider jwtProvider;

	@BeforeEach
	void setup() {
		jwtProvider = new JwtProvider(TEST_SECRET_KEY);
	}

	@Test
	@DisplayName("JWT 토큰을 발급하면 토큰을 반환해준다.")
	void given_login_member_when_generate_token_then_return_token() {
		// given
		MemberResponse memberResponse = getMemberResponse();

		// when
		String token = jwtProvider.generateToken(memberResponse);

		// then
		assertThat(token).isNotEmpty();
	}

	@Test
	void parseMemberIdFrom_shouldFailForTamperedToken() {
		// given
		MemberResponse memberResponse = getMemberResponse();
		String validToken = jwtProvider.generateToken(memberResponse);

		// when
		String tamperedToken = validToken.substring(0, validToken.length() - 2) + "aa";

		// then
		assertThatThrownBy(() -> jwtProvider.parseLoginMemberFromToken(tamperedToken))
			.isInstanceOf(SecurityException.class);
	}

	private static @NotNull MemberResponse getMemberResponse() {
		return new MemberResponse(1L, "test", "test@mail.com", Role.ADMIN);
	}

}
