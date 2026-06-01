package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthControllerTest extends BaseControllerTest {

    @DisplayName("로그인 후 토큰을 발급받고 사용자 정보를 확인한다")
    @Test
    void login_and_check_token() {
        // given
        String token = createToken("admin@email.com", "password");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        // then
        assertThat(token).isNotBlank();
        assertThat(response.body().jsonPath().getString("name")).isEqualTo("어드민");
    }

    @DisplayName("일반 유저는 어드민 페이지에 접근할 수 없고, 어드민은 접근할 수 있다")
    @Test
    void admin_access_control() {
        // given
        String brownToken = createToken("brown@email.com", "password");
        String adminToken = createToken("admin@email.com", "password");

        // when & then
        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/admin")
                .then().log().all()
                .statusCode(401);

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/admin")
                .then().log().all()
                .statusCode(200);
    }
}
