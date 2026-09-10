package roomescape.domain;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PageHttpTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void ADMIN은_admin_페이지에_접근할_수_있다() {
        // given
        String adminToken = createToken("admin@email.com", "password");

        // when & then
        RestAssured.given()
                .cookie("token", adminToken)
                .when().get("/admin")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void USER는_admin_페이지에_접근할_수_없다() {
        // given
        String userToken = createToken("brown@email.com", "password");

        // when & then
        RestAssured.given()
                .cookie("token", userToken)
                .when().get("/admin")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 토큰_없이_admin_페이지에_접근할_수_없다() {
        RestAssured.given()
                .when().get("/admin")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 메인_페이지에_접근할_수_있다() {
        RestAssured.given()
                .when().get("/")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 로그인_페이지에_접근할_수_있다() {
        RestAssured.given()
                .when().get("/login")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원가입_페이지에_접근할_수_있다() {
        RestAssured.given()
                .when().get("/signup")
                .then()
                .statusCode(HttpStatus.OK.value());
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
