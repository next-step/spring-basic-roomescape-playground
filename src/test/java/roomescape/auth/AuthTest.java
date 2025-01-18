package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthTest {
    private static final String USERNAME_FIELD = "email";
    private static final String PASSWORD_FIELD = "password";
    private static final String VALID_EMAIL = "admin@email.com";
    private static final String VALID_PASSWORD = "password";
    private static final String INVALID_EMAIL = "email@email.com";
    private static final String INVALID_PASSWORD = "1234";

    @Test
    void 로그인_성공() {
        Map<String, String> params = new HashMap<>();
        params.put(USERNAME_FIELD, VALID_EMAIL);
        params.put(PASSWORD_FIELD, VALID_PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .extract();

        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(token).isNotBlank();
    }

    @Test
    void 이메일과_비밀번호가_일치하지_않는_경우_예외가_발생한다() {
        Map<String, String> params = new HashMap<>();
        params.put(USERNAME_FIELD, INVALID_EMAIL);
        params.put(PASSWORD_FIELD, INVALID_PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat(response.body().asString()).isEqualTo("Invalid email or password");
    }
}
