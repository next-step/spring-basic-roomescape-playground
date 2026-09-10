package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Test
    void 일단계() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "password");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];

        assertThat(token).isNotBlank();

        ExtractableResponse<Response> checkResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(checkResponse.body().jsonPath().getString("name")).isEqualTo("어드민");
    }

    @Nested
    class 인증_실패 {

        @ParameterizedTest
        @CsvSource({
                "wrong@email.com, password",
                "admin@email.com, wrong-password"
        })
        void 이메일이나_비밀번호가_틀리면_401(String email, String password) {
            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/login")
                    .then().log().all()
                    .statusCode(401);
        }

        @Test
        void 토큰_없이_인증_정보를_조회하면_401() {
            RestAssured.given().log().all()
                    .when().get("/login/check")
                    .then().log().all()
                    .statusCode(401);
        }
    }
}