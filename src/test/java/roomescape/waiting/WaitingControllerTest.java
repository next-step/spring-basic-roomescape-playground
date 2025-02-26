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
    void 예약이_존재하며_본인의_예약_및_예약_대기가_존재하지_않는_경우_예약_대기_생성에_성공한다() {
        // given
        String brownToken = createToken("brown@email.com", "password");
        Map<String, String> waitingRequest = createPostWaitingRequest("브라운", "2024-03-01", "1", "1");

        // when
        ExtractableResponse<Response> response = sendPostWaitingRequest(brownToken, waitingRequest);
        WaitingResponse waitingResponse = response.as(WaitingResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(waitingResponse.name()).isEqualTo("브라운");
        assertThat(waitingResponse.date()).isEqualTo("2024-03-01");
        assertThat(waitingResponse.time()).isEqualTo("10:00");
        assertThat(waitingResponse.theme()).isEqualTo("테마1");
    }

    @Test
    void 본인의_예약이_존재하는_경우_예약_대기_생성에_실패한다() {
        // given
        String adminToken = createToken("admin@email.com", "password");
        Map<String, String> waitingRequest = createPostWaitingRequest("어드민", "2024-03-01", "1", "1");

        // when
        ExtractableResponse<Response> response = sendPostWaitingRequest(adminToken, waitingRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    void 본인의_예약_대기가_존재하는_경우_예약_대기_생성에_실패한다() {
        // given
        String brownToken = createToken("brown@email.com", "password");
        Map<String, String> waitingRequest = createPostWaitingRequest("어드민", "2024-03-01", "1", "1");
        sendPostWaitingRequest(brownToken, waitingRequest);

        // when
        ExtractableResponse<Response> duplicateResponse = sendPostWaitingRequest(brownToken, waitingRequest);

        // then
        assertThat(duplicateResponse.statusCode()).isEqualTo(400);
    }

    @Test
    void 예약이_존재하지_않는_경우_예약_대기_생성에_실패한다() {
        // given
        String brownToken = createToken("brown@email.com", "password");
        Map<String, String> waitingRequest = createPostWaitingRequest("브라운", "2024-03-02", "1", "1");

        // when
        ExtractableResponse<Response> response = sendPostWaitingRequest(brownToken, waitingRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(400);
    }

    private Map<String, String> createPostWaitingRequest(String name, String date, String timeId, String themeId) {
        Map<String, String> request = new HashMap<>();
        request.put("name", name);
        request.put("date", date);
        request.put("timeId", timeId);
        request.put("themeId", themeId);
        return request;
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
