package roomescape.auth;


import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import roomescape.auth.jwt.MemberTokenDto;
import roomescape.auth.jwt.TokenService;

@SpringBootTest
@TestPropertySource(properties = {
	"roomescape.auth.jwt.secret.key=thisistestkeythisistestkeythisistestkeythisistestkeythisistestkey",
	"roomescape.auth.jwt.secret.expiration=1000"
})
class TokenServiceTest {

	@Autowired
	private TokenService tokenService;

	@Test
	void 토큰을_생성할_수_있다() {
		MemberTokenDto memberTokenDto = new MemberTokenDto(1L, "test", "USER");

		TokenResponse tokenResponse = tokenService.createAccessToken(memberTokenDto);
		MemberTokenDto parseTokenInfo = tokenService.extractMemberResponseFromToken(tokenResponse.token());

		assertThat(tokenResponse.token()).isNotEmpty();
		assertThat(memberTokenDto.id()).isEqualTo(parseTokenInfo.id());
		assertThat(memberTokenDto.name()).isEqualTo(parseTokenInfo.name());
		assertThat(memberTokenDto.role()).isEqualTo(parseTokenInfo.role());
	}

	@Test
	void 정상적인_토큰을_검증할_수_있다() {
		MemberTokenDto memberTokenDto = new MemberTokenDto(1L, "test", "USER");
		TokenResponse tokenResponse = tokenService.createAccessToken(memberTokenDto);

		boolean isValid = tokenService.validToken(tokenResponse.token());
		assertThat(isValid).isTrue();
	}

	@Test
	void 만료된_토큰을_검증할_수_있다() throws InterruptedException {
		MemberTokenDto memberTokenDto = new MemberTokenDto(1L, "test", "USER");
		TokenResponse tokenResponse = tokenService.createAccessToken(memberTokenDto);

		Thread.sleep(1500);
		assertThatThrownBy(() -> tokenService.validToken(tokenResponse.token()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 조작된_토큰을_검증할_수_있다() {
		MemberTokenDto memberTokenDto = new MemberTokenDto(1L, "test", "USER");
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
		MemberTokenDto memberTokenDto = new MemberTokenDto(1L, "test", "USER");
		TokenResponse tokenResponse = tokenService.createAccessToken(memberTokenDto);

		MemberTokenDto parseTokenInfo = tokenService.extractMemberResponseFromToken(tokenResponse.token());

		assertThat(memberTokenDto.id()).isEqualTo(parseTokenInfo.id());
		assertThat(memberTokenDto.name()).isEqualTo(parseTokenInfo.name());
		assertThat(memberTokenDto.role()).isEqualTo(parseTokenInfo.role());
	}


}
