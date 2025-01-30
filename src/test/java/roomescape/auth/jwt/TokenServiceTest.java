package roomescape.auth.jwt;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.auth.TestTimeProvider;
import roomescape.auth.TokenResponse;

@SpringBootTest
class TokenServiceTest {

	private TokenService tokenService;
	private TestTimeProvider timeProvider;
	private MemberTokenDto memberTokenDto;

	@BeforeEach
	void init() {
		timeProvider = new TestTimeProvider(new Date());
		tokenService = new TokenService("thisistestkeythisistestkeythisistestkeythisistestkeythisistestkey", 1000L, timeProvider);
		memberTokenDto = new MemberTokenDto(1L, "test", "USER");
	}

	@Test
	void 토큰을_생성할_수_있다() {
		TokenResponse tokenResponse = tokenService.createAccessToken(memberTokenDto);
		MemberTokenDto parseTokenInfo = tokenService.extractMemberResponseFromToken(tokenResponse.token());

		assertThat(tokenResponse.token()).isNotEmpty();
		assertThat(memberTokenDto.id()).isEqualTo(parseTokenInfo.id());
		assertThat(memberTokenDto.name()).isEqualTo(parseTokenInfo.name());
		assertThat(memberTokenDto.role()).isEqualTo(parseTokenInfo.role());
	}

	@Test
	void 정상적인_토큰을_검증할_수_있다() {
		TokenResponse tokenResponse = tokenService.createAccessToken(memberTokenDto);

		boolean isValid = tokenService.validToken(tokenResponse.token());
		assertThat(isValid).isTrue();
	}

	@Test
	void 만료된_토큰을_검증할_수_있다() {
		timeProvider.decreaseTime(1500);
		TokenResponse tokenResponse = tokenService.createAccessToken(memberTokenDto);

		assertThatThrownBy(() -> tokenService.validToken(tokenResponse.token()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 조작된_토큰을_검증할_수_있다() {
		TokenResponse tokenResponse = tokenService.createAccessToken(memberTokenDto);

		String invalidToken = tokenResponse.token().concat("123");
		assertThatThrownBy(() -> tokenService.validToken(invalidToken))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 유효하지_않은_토큰을_검증할_수_있다() {
		String token = "abcdefg";
		assertThatThrownBy(() -> tokenService.validToken(token))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 토큰에서_사용자_정보를_가져올_수_있다() {
		TokenResponse tokenResponse = tokenService.createAccessToken(memberTokenDto);

		MemberTokenDto parseTokenInfo = tokenService.extractMemberResponseFromToken(tokenResponse.token());

		assertThat(memberTokenDto.id()).isEqualTo(parseTokenInfo.id());
		assertThat(memberTokenDto.name()).isEqualTo(parseTokenInfo.name());
		assertThat(memberTokenDto.role()).isEqualTo(parseTokenInfo.role());
	}


}
