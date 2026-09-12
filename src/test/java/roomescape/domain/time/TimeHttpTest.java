package roomescape.domain.time;

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
public class TimeHttpTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void 시간_생성에_성공한다() {
        // given
        String token = createToken("admin@email.com", "password");
        Map<String, String> params = new HashMap<>();
        params.put("value", "11:00");

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
                .body("value", is("11:00"));
    }

    @Test
    void 시간은_value가_빈_채로_생성할_수_없다() {
        // given
        String token = createToken("admin@email.com", "password");
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
        // schema.sql 시드 시간 6건
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(6));
    }

    @Test
    void 시간_삭제에_성공한다() {
        // given
        String token = createToken("admin@email.com", "password");

        // when
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().delete("/times/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(5));
    }

    @Test
    void 일반_유저는_시간을_삭제할_수_없다() {
        // given
        String token = createToken("brown@email.com", "password");

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().delete("/times/1")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 예약_가능_시간_조회에_성공한다() {
        // given
        String date = LocalDate.now().plusDays(1).toString();

        // when & then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/available-times?date=" + date + "&themeId=1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(6))
                .body("time", hasItems("10:00", "20:00"));
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
