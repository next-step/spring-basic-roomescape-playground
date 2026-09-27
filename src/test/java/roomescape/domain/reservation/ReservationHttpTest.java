package roomescape.domain.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationHttpTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void 예약_생성에_성공한다() {
        // given
        String token = createToken("admin@dummy.com", "dummy");
        Time time = saveTime();
        Theme theme = saveTheme();
        Map<String, Object> params = reservationParams("더미_유저", time, theme);

        // when & then
        RestAssured.given()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/reservations"))
                .body("id", notNullValue())
                .body("name", is("더미_유저"))
                .body("theme", is("Dummy"))
                .body("time", is("03:00"));
    }

    @Test
    void 일반_유저가_이름을_지정하면_400을_반환한다() {
        // given
        String token = createToken("user@dummy.com", "dummy");
        Map<String, Object> params = reservationParams("Alice", saveTime(), saveTheme());

        // when & then
        RestAssured.given()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 이름이_없으면_로그인한_사용자의_이름으로_예약된다() {
        // given
        String token = createToken("user@dummy.com", "dummy");
        Map<String, Object> params = reservationParams(null, saveTime(), saveTheme());

        // when & then
        RestAssured.given()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", is("더미_유저"));
    }

    @Test
    void 이름과_토큰이_모두_없으면_401을_반환한다() {
        // given
        Map<String, Object> params = reservationParams(null, saveTime(), saveTheme());

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
        String token = createToken("admin@dummy.com", "dummy");
        Map<String, Object> params = reservationParams("Alice", saveTime(), saveTheme());
        params.put("date", null);

        // when & then
        postReservationExpectingBadRequest(token, params);
    }

    @Test
    void 예약은_과거_날짜로_생성할_수_없다() {
        // given
        String token = createToken("admin@dummy.com", "dummy");
        Map<String, Object> params = reservationParams("Alice", saveTime(), saveTheme());
        params.put("date", LocalDate.now().minusDays(1).toString());

        // when & then
        postReservationExpectingBadRequest(token, params);
    }

    @Test
    void 예약은_time이_빈_채로_생성할_수_없다() {
        // given
        String token = createToken("admin@dummy.com", "dummy");
        Map<String, Object> params = reservationParams("Alice", saveTime(), saveTheme());
        params.put("time", null);

        // when & then
        postReservationExpectingBadRequest(token, params);
    }

    @Test
    void 예약은_theme이_빈_채로_생성할_수_없다() {
        // given
        String token = createToken("admin@dummy.com", "dummy");
        Map<String, Object> params = reservationParams("Alice", saveTime(), saveTheme());
        params.put("theme", null);

        // when & then
        postReservationExpectingBadRequest(token, params);
    }

    @Test
    void 예약_목록_조회에_성공한다() {
        // given
        String token = createToken("admin@dummy.com", "dummy");

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }

    @Test
    void 로그인_없이_예약_목록을_조회하면_401을_반환한다() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 일반_유저가_예약_목록을_조회하면_403을_반환한다() {
        // given
        String token = createToken("user@dummy.com", "dummy");

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 예약_삭제에_성공한다() {
        // given
        String token = createToken("admin@dummy.com", "dummy");

        // when
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().delete("/reservations/" + 1L)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void 로그인_없이_예약을_삭제할_수_없다() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete("/reservations/1")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    private Time saveTime() {
        return timeRepository.save(new Time(LocalTime.of(3, 0)));
    }

    private Theme saveTheme() {
        return themeRepository.save(new Theme("Dummy",  "it is Dummy for Test"));
    }

    private Map<String, Object> reservationParams(String name, Time time, Theme theme) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("time", time.getId());
        params.put("theme", theme.getId());
        return params;
    }

    private void postReservationExpectingBadRequest(String token, Map<String, Object> params) {
        RestAssured.given()
                .body(params)
                .cookie("token", token)
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
                .extract().cookie("token");
    }
}
