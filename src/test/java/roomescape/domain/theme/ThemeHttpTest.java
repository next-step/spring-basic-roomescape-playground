package roomescape.domain.theme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeHttpTest {

    private final String name = "새테마";
    private final String description = "새테마입니다.";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void 테마_생성에_성공한다() {
        // given
        String token = createToken("admin@email.com", "password");
        Map<String, String> params = themeParams(name, description);

        // when & then
        RestAssured.given()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/themes")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/themes"))
                .body("id", notNullValue())
                .body("name", is(name))
                .body("description", is(description));
    }

    @Test
    void 일반_유저는_테마를_생성할_수_없다() {
        // given
        String token = createToken("brown@email.com", "password");
        Map<String, String> params = themeParams(name, description);

        // when & then
        RestAssured.given()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/themes")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 테마는_name이_빈_채로_생성할_수_없다() {
        // given
        String token = createToken("admin@email.com", "password");

        // name == null
        postThemeExpectingBadRequest(token, themeParams(null, description));

        // name == ""
        postThemeExpectingBadRequest(token, themeParams("", description));

        // name == " "
        postThemeExpectingBadRequest(token, themeParams(" ", description));
    }

    @Test
    void 테마는_description이_빈_채로_생성할_수_없다() {
        // given
        String token = createToken("admin@email.com", "password");

        // description == null
        postThemeExpectingBadRequest(token, themeParams(name, null));

        // description == ""
        postThemeExpectingBadRequest(token, themeParams(name, ""));

        // description == " "
        postThemeExpectingBadRequest(token, themeParams(name, " "));
    }

    @Test
    void 테마_목록_조회에_성공한다() {
        // schema.sql 시드 테마 3건
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @Test
    void 테마_삭제에_성공한다() {
        // given
        String token = createToken("admin@email.com", "password");

        // when
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().delete("/themes/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }

    private Map<String, String> themeParams(String name, String description) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", description);
        return params;
    }

    private void postThemeExpectingBadRequest(String token, Map<String, String> params) {
        RestAssured.given()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/themes")
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
