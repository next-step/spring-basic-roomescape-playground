package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.entity.RefreshToken;
import roomescape.auth.repository.RefreshTokenRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthApiTest {

    private final static String ACCESS_TOKEN = "access-token";
    private final static String REFRESH_TOKEN = "refresh-token";

    /*
    member
    +----+--------+------------------+----------+-------+
    | id | name   | email            | password | role  |
    +----+--------+------------------+----------+-------+
    | 1  | 어드민  | admin@email.com  | password | ADMIN |
    | 2  | 브라운  | brown@email.com  | password | USER  |
    +----+--------+------------------+----------+-------+

    time                        theme
    +----+------------+        +----+--------+---------------+
    | id | time_value |        | id | name   | description   |
    +----+------------+        +----+--------+---------------+
    | 1  | 10:00      |        | 1  | 테마1   | 테마1입니다.   |
    | 2  | 12:00      |        | 2  | 테마2   | 테마2입니다.   |
    | 3  | 14:00      |        | 3  | 테마3   | 테마3입니다.   |
    | 4  | 16:00      |        +----+--------+---------------+
    | 5  | 18:00      |
    | 6  | 20:00      |
    +----+------------+

    reservation
    +----+-----------+------------+---------+----------+
    | id | member_id | date       | time_id | theme_id |
    +----+-----------+------------+---------+----------+
    | 1  | 1         | 2024-03-01 | 1       | 1        |
    | 2  | 1         | 2024-03-01 | 2       | 2        |
    | 3  | 1         | 2024-03-01 | 3       | 3        |
    | 4  | 2         | 2024-03-01 | 1       | 2        |
    +----+-----------+------------+---------+----------+
    */

    @Test
    @DisplayName("존재하는 계정으로 로그인 시, 200과 함께 access/refresh 토큰 쿠키가 발급된다.")
    void loginSuccess_returnsTokenCookies() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "password");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        // then
        String accessToken = response.cookie(ACCESS_TOKEN);
        String refreshToken = response.cookie(REFRESH_TOKEN);
        assertThat(accessToken).isNotBlank();
        assertThat(refreshToken).isNotBlank();
    }

    @Test
    @DisplayName("존재하지 않는 email로 로그인 시도 시, 401을 반환한다")
    void login_withNonExistentEmail_returns401() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("email", "wrong@email.com");
        params.put("password", "password");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    @DisplayName("잘못된 password로 로그인 시도 시, 401을 반환한다")
    void login_withWrongPassword_returns401() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "wrongPassword");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Autowired
    RefreshTokenRepository repository;

    @Test
    @DisplayName("로그인 성공 시, refresh token이 DB에 저장된다,")
    void loginSuccess_savesRefreshTokenInDb() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "password");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        // then
        String refreshToken = response.cookie(REFRESH_TOKEN);
        RefreshToken savedToken = repository.findByToken(refreshToken)
                .orElseThrow(() -> new AssertionError("저장된 Refresh Token이 없습니다."));
        assertThat(savedToken.getToken()).isEqualTo(refreshToken);
    }
}
