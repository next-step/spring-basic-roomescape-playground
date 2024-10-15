package roomescape;

import roomescape.global.auth.JwtUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomescape.member.Member;
import roomescape.reservation.controller.dto.MyReservationResponse;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.controller.dto.WaitingResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class MissionStepTest {

    @Autowired
    private JwtUtils jwtTokenProvider;

    @Value("${jwt.secret-key")
    private String secretKey;

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

    @Test
    void 이단계() {
        String token = jwtTokenProvider.createToken(
            new Member(
                1L,
                "어드민",
                "admin@email.com",
                "password",
                "ADMIN"
            )
        );

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .cookie("token", token)
            .contentType(ContentType.JSON)
            .post("/reservations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).getName()).isEqualTo("어드민");

        params.put("name", "브라운");

        ExtractableResponse<Response> adminResponse = RestAssured.given().log().all()
            .body(params)
            .cookie("token", token)
            .contentType(ContentType.JSON)
            .post("/reservations")
            .then().log().all()
            .extract();

        assertThat(adminResponse.statusCode()).isEqualTo(201);
        assertThat(adminResponse.as(ReservationResponse.class).getName()).isEqualTo("브라운");
    }

    @Test
    void 삼단계() {
        String brownToken = jwtTokenProvider.createToken(
            new Member(
                2L,
                "브라운",
                "brown@email.com",
                "password",
                "USER"
            )
        );

        RestAssured.given().log().all()
            .cookie("token", brownToken)
            .get("/admin")
            .then().log().all()
            .statusCode(401);

        String adminToken = jwtTokenProvider.createToken(
            new Member(
                1L,
                "어드민",
                "admin@email.com",
                "password",
                "ADMIN"
            )
        );

        RestAssured.given().log().all()
            .cookie("token", adminToken)
            .get("/admin")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    void 오단계() {
        String adminToken = jwtTokenProvider.createToken(
            new Member(
                1L,
                "어드민",
                "admin@email.com",
                "password",
                "ADMIN"
            )
        );

        List<MyReservationResponse> reservations = RestAssured.given().log().all()
            .cookie("token", adminToken)
            .get("/reservations-mine")
            .then().log().all()
            .statusCode(200)
            .extract().jsonPath().getList(".", MyReservationResponse.class);

        assertThat(reservations).hasSize(3);
    }

    @Test
    void 육단계() {
        String brownToken = jwtTokenProvider.createToken(
            new Member(
                2L,
                "브라운",
                "brown@email.com",
                "password",
                "USER"
            )
        );

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        // 예약 대기 생성
        WaitingResponse waiting = RestAssured.given().log().all()
            .body(params)
            .cookie("token", brownToken)
            .contentType(ContentType.JSON)
            .post("/waitings")
            .then().log().all()
            .statusCode(201)
            .extract().as(WaitingResponse.class);

        // 내 예약 대기 목록 조회
        List<MyReservationResponse> myReservations = RestAssured.given().log().all()
            .body(params)
            .cookie("token", brownToken)
            .contentType(ContentType.JSON)
            .get("reservations-mine")
            .then().log().all()
            .statusCode(200)
            .extract().jsonPath().getList(".", MyReservationResponse.class);

        String status = myReservations.stream()
            .filter(it -> it.id() == waiting.id())
            .filter(it -> !it.status().equals("예약"))
            .findFirst()
            .map(it -> it.status())
            .orElse(null);

        assertThat(status).isEqualTo("1번째 예약대기");
    }

    @Test
    void 칠단계() {
        Component componentAnnotation = JwtUtils.class.getAnnotation(Component.class);
        assertThat(componentAnnotation).isNull();
    }

    @Test
    void 팔단계() {
        assertThat(secretKey).isNotBlank();
    }
}