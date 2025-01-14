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
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "1234";

    @Test
    void login() {
        Map<String, String> params = new HashMap<>();
        params.put(USERNAME_FIELD, EMAIL);
        params.put(PASSWORD_FIELD, PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];

        assertThat(token).isNotBlank();
    }

}
