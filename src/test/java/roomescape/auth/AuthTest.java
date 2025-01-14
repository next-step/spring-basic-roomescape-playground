package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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

        AuthInfo authInfo = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().as(AuthInfo.class);

        assertThat(authInfo.email()).isEqualTo(EMAIL);
    }

}
