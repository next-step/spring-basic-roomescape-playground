package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class WaitingApiTest extends ApiTest {

    private final static String ACCESS_TOKEN = "access-token";

    @Test
    void 예약이_존재하는_시간에_대기를_신청하면_대기가_생성된다() {
        // given
        String accessToken = 로그인_시도("brown@email.com", "password")
                .cookie(ACCESS_TOKEN);

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(ACCESS_TOKEN, accessToken)
                .body(params)
                .when().post("/waitings")
                .then().log().all()
                .extract();

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(response.statusCode()).isEqualTo(201);
            softAssertions.assertThat(response.jsonPath().getLong("waitingNumber")).isEqualTo(1L);
        });
    }

    @Test
    void 대기를_삭제하면_204를_반환한다() {
        // given
        String accessToken = 로그인_시도("brown@email.com", "password")
                .cookie(ACCESS_TOKEN);

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        Long waitingId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(ACCESS_TOKEN, accessToken)
                .body(params)
                .when().post("/waitings")
                .then().log().all()
                .extract()
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie(ACCESS_TOKEN, accessToken)
                .when().delete("/waitings/" + waitingId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(204);
    }
}
