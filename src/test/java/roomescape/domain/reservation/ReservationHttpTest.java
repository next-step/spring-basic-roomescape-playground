package roomescape.domain.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationHttpTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void 예약_생성에_성공한다() {
        // given
        String token = createToken("admin@email.com", "password");
        Map<String, Object> params = reservationParams("Alice");

        // when & then
        RestAssured.given()
                .body(params)
                .cookie("JSESSIONID", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/reservations"))
                .body("id", notNullValue())
                .body("name", is("Alice"))
                .body("theme", is("테마1"))
                .body("time", is("10:00"));
    }

    @Test
    void 일반_유저가_이름을_지정하면_403을_반환한다() {
        // given
        String token = createToken("brown@email.com", "password");
        Map<String, Object> params = reservationParams("Alice");

        // when & then
        RestAssured.given()
                .body(params)
                .cookie("JSESSIONID", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 이름이_없으면_로그인한_사용자의_이름으로_예약된다() {
        // given
        String token = createToken("admin@email.com", "password");
        Map<String, Object> params = reservationParams(null);

        // when & then
        RestAssured.given()
                .body(params)
                .cookie("JSESSIONID",token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", is("어드민"));
    }

    @Test
    void 이름과_토큰이_모두_없으면_401을_반환한다() {
        // given
        Map<String, Object> params = reservationParams(null);

        // when & then
        RestAssured.given()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 예약은_date가_빈_채로_생성할_수_없다() {
        // given
        Map<String, Object> params = reservationParams("Alice");
        params.put("date", null);

        // when & then
        postReservationExpectingBadRequest(params);
    }

    @Test
    void 예약은_과거_날짜로_생성할_수_없다() {
        // given
        Map<String, Object> params = reservationParams("Alice");
        params.put("date", LocalDate.now().minusDays(1).toString());

        // when & then
        postReservationExpectingBadRequest(params);
    }

    @Test
    void 예약은_time이_빈_채로_생성할_수_없다() {
        // given
        Map<String, Object> params = reservationParams("Alice");
        params.put("time", null);

        // when & then
        postReservationExpectingBadRequest(params);
    }

    @Test
    void 예약은_theme이_빈_채로_생성할_수_없다() {
        // given
        Map<String, Object> params = reservationParams("Alice");
        params.put("theme", null);

        // when & then
        postReservationExpectingBadRequest(params);
    }

    @Test
    void 예약_목록_조회에_성공한다() {
        // schema.sql 시드 예약 3건
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @Test
    void 예약_삭제에_성공한다() {
        // when
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete("/reservations/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }

    private Map<String, Object> reservationParams(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("time", 1);
        params.put("theme", 1);
        return params;
    }

    private void postReservationExpectingBadRequest(Map<String, Object> params) {
        RestAssured.given()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private String createToken(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().cookie("JSESSIONID");
    }
}
