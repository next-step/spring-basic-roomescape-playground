package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import roomescape.auth.entity.RefreshToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class AuthApiTest extends ApiTest {

    private final static String ACCESS_TOKEN = "access-token";
    private final static String REFRESH_TOKEN = "refresh-token";

    @Test
    void 존재하는_계정으로_로그인하면_액세스_리프레시_토큰_쿠키가_발급된다() {
        // given-when
        ExtractableResponse<Response> response = 로그인_시도("admin@email.com", "password");

        // then
        String accessToken = response.cookie(ACCESS_TOKEN);
        String refreshToken = response.cookie(REFRESH_TOKEN);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(response.statusCode()).isEqualTo(200);
            softAssertions.assertThat(accessToken).isNotBlank();
            softAssertions.assertThat(refreshToken).isNotBlank();
        });
    }

    @Test
    void 존재하지_않는_이메일로_로그인하면_401을_반환한다() {
        // given-when
        ExtractableResponse<Response> response = 로그인_시도("wrong@email.com", "password");

        // then
        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    void 잘못된_비밀번호로_로그인하면_401을_반환한다() {
        // given-when
        ExtractableResponse<Response> response = 로그인_시도("wrong@email.com", "wrongPassword");

        // then
        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    void 로그인에_성공하면_리프레시_토큰이_DB에_저장된다() {
        // given-when
        ExtractableResponse<Response> response = 로그인_시도("admin@email.com", "password");

        // then
        String refreshToken = response.cookie(REFRESH_TOKEN);
        RefreshToken savedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AssertionError("저장된 Refresh Token이 없습니다."));
        assertThat(savedToken.getToken()).isEqualTo(refreshToken);
    }

    @Test
    void 엑세스_토큰_재발급_시_리프레시_토큰도_새롭게_저장한다() {
        // given
        ExtractableResponse<Response> loginResponse = 로그인_시도("admin@email.com", "password");

        String originalRefreshToken = loginResponse.cookie(REFRESH_TOKEN);
        Long originalTokenId = refreshTokenRepository.findByToken(originalRefreshToken)
                .orElseThrow().getId();

        // when
        ExtractableResponse<Response> reissueResponse = RestAssured.given().log().all()
                .cookie(REFRESH_TOKEN, originalRefreshToken)
                .when().post("/login/refresh")
                .then().log().all()
                .extract();

        // then
        String newRefreshToken = reissueResponse.cookie(REFRESH_TOKEN);
        RefreshToken savedToken = refreshTokenRepository.findByToken(newRefreshToken)
                .orElseThrow(() -> new AssertionError("재발급된 리프레시 토큰이 DB에 없습니다."));
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(reissueResponse.statusCode()).isEqualTo(200);
            softAssertions.assertThat(newRefreshToken).isNotBlank();
            softAssertions.assertThat(refreshTokenRepository.findById(originalTokenId)).isEmpty();
            softAssertions.assertThat(savedToken.getId()).isNotEqualTo(originalTokenId);
            softAssertions.assertThat(savedToken.getToken()).isEqualTo(newRefreshToken);
        });
    }

    @Test
    void 로그아웃하면_리프레시_토큰도_DB에서_제거된다() {
        // given
        ExtractableResponse<Response> loginResponse = 로그인_시도("admin@email.com", "password");

        String accessToken = loginResponse.cookie(ACCESS_TOKEN);
        String refreshToken = loginResponse.cookie(REFRESH_TOKEN);
        Long tokenId = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow().getId();

        // when
        ExtractableResponse<Response> logoutResponse = RestAssured.given().log().all()
                .cookie(ACCESS_TOKEN, accessToken)
                .when().post("/logout")
                .then().log().all()
                .extract();

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(logoutResponse.statusCode()).isEqualTo(200);
            softAssertions.assertThat(refreshTokenRepository.findById(tokenId)).isEmpty();
        });
    }
}
