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
import roomescape.reservation.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {
    private static final String PASSWORD = "password";

    private String loginAndGetToken(String email) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", PASSWORD);

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
        String token = loginAndGetToken("admin@email.com");
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("로그인 시 정상적으로 사용자 정보가 반환된다.")
    void shouldReturnUserInfo_whenLogin() {
        String token = loginAndGetToken("admin@email.com");

        ExtractableResponse<Response> checkResponse = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(checkResponse.body().jsonPath().getString("name")).isEqualTo("어드민");
    }

    @Test
    @DisplayName("로그인한 사용자의 예약이 정상적으로 이루어진다.")
    void shouldReservation_whenLoginMemberInfo() {
        String token = loginAndGetToken("admin@email.com");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).name()).isEqualTo("어드민");
    }

    @Test
    @DisplayName("예약을 위해 입력한 이름이 존재할 경우 입력한 이름으로 예약이 정상적으로 이루어진다.")
    void shouldReservation_whenInputName() {
        String token = loginAndGetToken("admin@email.com");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");
        params.put("name", "브라운");

        ExtractableResponse<Response> adminResponse = RestAssured
                .given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(adminResponse.statusCode()).isEqualTo(201);
        assertThat(adminResponse.as(ReservationResponse.class).name()).isEqualTo("브라운");
    }

    @Test
    @DisplayName("어드민 권한을 가진 사용자만 어드민 페이지에 접근할 수 있다.")
    void shouldApproachAdminPage_whenOnlyAdminRole() {
        String brownToken = loginAndGetToken("brown@email.com");

        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/admin")
                .then().log().all()
                .statusCode(401);

        String adminToken = loginAndGetToken("admin@email.com");

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/admin")
                .then().log().all()
                .statusCode(200);
    }
}
