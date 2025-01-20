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
class LoginTest {
    @Test
    void 로그인_성공() {
        //given
        Map<String, String> validCredentials = new HashMap<>();
        validCredentials.put("email", "admin@email.com");
        validCredentials.put("password", "password");

        //when
        ExtractableResponse<Response> response = sendLoginRequest(validCredentials);

        //then
        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(token).isNotBlank();
    }

    @Test
    void 이메일과_비밀번호가_일치하지_않는_경우_예외가_발생한다() {
        //given
        Map<String, String> invalidCredentials = new HashMap<>();
        invalidCredentials.put("email", "email@email.com");
        invalidCredentials.put("password", "1234");

        //when
        ExtractableResponse<Response> response = sendLoginRequest(invalidCredentials);

        //then
        assertThat(response.statusCode()).isEqualTo(401);
        assertThat(response.body().asString()).isEqualTo("Invalid email or password");
    }

    private ExtractableResponse<Response> sendLoginRequest(Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .extract();
    }
}
