package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Test
    @DisplayName("로그인에 성공하면 토큰이 담긴 쿠키를 응답한다")
    void test_로그인이_성공하면_쿠기헤더에_토큰을_담아서_클라이언트한테_전달() {
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
    }

    @Test
    @DisplayName("발급받은 토큰으로 로그인한 사용자 정보를 조회한다")
    void test_최초_로그인_이후에_넘겨주는_쿠키헤더에_토큰을_통해_사용자정보를_조회() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "password");

        String token = RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/login")
            .then().log().all()
            .statusCode(200)
            .extract()
            .headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];

        ExtractableResponse<Response> checkResponse = RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .cookie("token", token)
            .when().get("/login/check")
            .then().log().all()
            .statusCode(200)
            .extract();

        assertThat(checkResponse.body().jsonPath().getString("name")).isEqualTo("어드민");
    }
}