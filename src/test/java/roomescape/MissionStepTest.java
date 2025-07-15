package roomescape;

import auth.JwtUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.waiting.dto.WaitingResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    /**
     * [추가] 로그인 요청을 보내고 토큰을 반환하는 헬퍼 메서드
     * @param email 로그인할 이메일
     * @param password 로그인할 비밀번호
     * @return 발급된 JWT 토큰
     */
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

        return response.response().getCookie("token");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM waiting");
        jdbcTemplate.execute("DELETE FROM reservation");
        jdbcTemplate.execute("DELETE FROM member");
        jdbcTemplate.execute("DELETE FROM theme");
        jdbcTemplate.execute("DELETE FROM time");

        jdbcTemplate.execute("ALTER TABLE waiting ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE time ALTER COLUMN id RESTART WITH 1");

        // Member (ID: 1=어드민, 2=브라운)
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN')");
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES ('브라운', 'brown@email.com', 'password', 'USER')");

        // Theme (ID: 1)
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('공포 테마', '매우 무서운 테마입니다.', 'thumbnail.jpg')");

        // Time (ID: 1)
        jdbcTemplate.update("INSERT INTO time (time) VALUES ('10:00')");

        // Reservation (어드민 계정으로 3개의 예약 생성 - 오단계 테스트용)
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, theme_id, time_id) VALUES ('2025-01-01', 1, 1, 1)");
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, theme_id, time_id) VALUES ('2025-01-02', 1, 1, 1)");
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, theme_id, time_id) VALUES ('2025-01-03', 1, 1, 1)");
    }

    @Test
    void 일단계() {
        String token = createToken("admin@email.com", "password");
        assertThat(token).isNotBlank();
    }

    @Test
    void 이단계() {
        String token = createToken("admin@email.com", "password");

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", 1);
        params.put("theme", 1);

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
        String brownToken = createToken("brown@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/admin")
                .then().log().all()
                .statusCode(401);

        String adminToken = createToken("admin@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 오단계() {
        String adminToken = createToken("admin@email.com", "password");

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
        String brownToken = createToken("brown@email.com", "password");

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

        // 내 예약 목록 조회
        List<MyReservationResponse> myReservations = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MyReservationResponse.class);

        // 예약 대기 상태 확인
        String status = myReservations.stream()
                .filter(it -> it.getReservationId() == waiting.getId())
                .filter(it -> !it.getStatus().equals("예약"))
                .findFirst()
                .map(it -> it.getStatus())
                .orElse(null);

        assertThat(status).isEqualTo("1번째 예약대기");
    }

    @Test
    void 칠단계() {
        Component componentAnnotation = JwtUtils.class.getAnnotation(Component.class);
        assertThat(componentAnnotation).isNull();
    }
}
