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
        // given
        String token = createToken("admin@email.com", "password");
        Map<String, String> request = createPostWaitingRequest();

        // when
        WaitingResponse response = sendPostWaitingRequest(token, request).as(WaitingResponse.class);

        // then
        assertThat(response.name()).isEqualTo("어드민");
        assertThat(response.date()).isEqualTo("2024-03-01");
        assertThat(response.time()).isEqualTo("10:00");
        assertThat(response.theme()).isEqualTo("테마1");
    }

    @Test
    void 같은_예약_대기가_존재하는_경우_예약_대기_생성에_실패한다() {
        // given
        String token = createToken("admin@email.com", "password");
        Map<String, String> request = createPostWaitingRequest();
        ExtractableResponse<Response> response = sendPostWaitingRequest(token, request);

        // when
        ExtractableResponse<Response> duplicateResponse = sendPostWaitingRequest(token, request);

        // then
        assertThat(duplicateResponse.statusCode()).isEqualTo(400);
    }

    private Map<String, String> createPostWaitingRequest() {
        Map<String, String> param = new HashMap<>();
        param.put("name", "어드민");
        param.put("date", "2024-03-01");
        param.put("timeId", "1");
        param.put("themeId", "1");
        return param;
    }

    private ExtractableResponse<Response> sendPostWaitingRequest(String token, Map<String, String> param) {
        return RestAssured.given().log().all()
                .cookie("token", token)
                .body(param)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all().extract();
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
