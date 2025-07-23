package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {
    private static final String TEST_EMAIL = "admin@email.com";
    private static final String TEST_PASSWORD = "password";

    private String loginAndGetToken() {
        Map<String, String> params = new HashMap<>();
        params.put("email", TEST_EMAIL);
        params.put("password", TEST_PASSWORD);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        return extractTokenFromSetCookie(response);
    }

    private String extractTokenFromSetCookie(ExtractableResponse<Response> response) {
        return response.headers().get("Set-Cookie")
                .getValue()
                .split(";")[0]
                .split("=")[1];
    }

    @Test
    @DisplayName("로그인이 정상적으로 이루어진다.")
    void shouldLogin_whenValidLoginData() {
        String token = loginAndGetToken();
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("로그인 시 정상적으로 사용자 정보가 반환된다.")
    void shouldReturnUserInfo_whenLogin() {
        String token = loginAndGetToken();

        ExtractableResponse<Response> checkResponse = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(checkResponse.body()
                .jsonPath()
                .getString("name"))
                .isEqualTo("어드민");
    }
}
