package roomescape.domain.time;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
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
public class TimeHttpTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void 시간_생성에_성공한다() {
        // given
        String token = createToken("admin@dummy.com", "dummy");
        Map<String, String> params = new HashMap<>();
        params.put("value", "10:00");

        // when & then
        RestAssured.given()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/times"))
                .body("id", notNullValue())
                .body("value", is("10:00"));
    }

    @Test
    void 시간은_value가_빈_채로_생성할_수_없다() {
        // given
        String token = createToken("admin@dummy.com", "dummy");
        Map<String, String> params = new HashMap<>();
        params.put("value", null);

        // when & then
        RestAssured.given()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 시간_목록_조회에_성공한다() {
        // given
        timeRepository.save(new Time(LocalTime.of(10, 0)));
        timeRepository.save(new Time(LocalTime.of(12, 0)));

        // when & then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3))
                .body("value", hasItems("00:00", "10:00", "12:00"));
    }

    @Test
    void 시간_삭제에_성공한다() {
        // given
        String token = createToken("admin@dummy.com", "dummy");
        Time savedTime = timeRepository.save(new Time(LocalTime.of(10, 0)));

        // when
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().delete("/times/" + savedTime.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }

    @Test
    void 일반_유저는_시간을_삭제할_수_없다() {
        // given
        String token = createToken("user@dummy.com", "dummy");
        Time savedTime = timeRepository.save(new Time(LocalTime.of(10, 0)));

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().delete("/times/" + savedTime.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 예약_가능_시간_조회에_성공한다() {
        // given
        timeRepository.save(new Time(LocalTime.of(10, 0)));
        timeRepository.save(new Time(LocalTime.of(12, 0)));
        Theme theme = themeRepository.save(new Theme("Dummy", "it is Dummy for test."));

        String date = LocalDate.now().plusDays(1).toString();

        // when & then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/available-times?date=" + date + "&themeId=" + theme.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3))
                .body("time", hasItems("00:00", "10:00", "12:00"));
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
