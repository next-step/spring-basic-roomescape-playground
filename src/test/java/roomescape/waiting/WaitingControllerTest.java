package roomescape.waiting;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class WaitingControllerTest {

    @Test
    void 예약_대기_생성_성공() {
        String token = createToken("admin@email.com", "password");

        Map<String, String> param = new HashMap<>();
        param.put("name", "어드민");
        param.put("date", "2024-03-01");
        param.put("timeId", "1");
        param.put("themeId", "1");

        WaitingResponse waitingResponse = RestAssured.given().log().all()
                .cookie("token", token)
                .body(param)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        assertThat(waitingResponse.name()).isEqualTo("어드민");
        assertThat(waitingResponse.date()).isEqualTo("2024-03-01");
        assertThat(waitingResponse.time()).isEqualTo("10:00");
        assertThat(waitingResponse.theme()).isEqualTo("테마1");
    }

    @Test
    void 같은_예약_대기가_존재하는_경우_예약_대기_생성에_실패한다() {
        String token = createToken("admin@email.com", "password");

        Map<String, String> param = new HashMap<>();
        param.put("name", "어드민");
        param.put("date", "2024-03-01");
        param.put("timeId", "1");
        param.put("themeId", "1");

        WaitingResponse waitingResponse = RestAssured.given().log().all()
                .cookie("token", token)
                .body(param)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        RestAssured.given().log().all()
                .cookie("token", token)
                .body(param)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(400);
    }

    private String createToken(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        return response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
    }
}
