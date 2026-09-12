package roomescape.member;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import roomescape.support.DatabaseTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;

@DatabaseTest
class MemberControllerTest {



    // 1. POST /members (성공)
    @Test
    void 회원을_정상적으로_생성한다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "신규회원");
        params.put("email", "new@email.com");
        params.put("password", "1234");
        params.put("role", "USER");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/members")
                .then().log().all()
                .statusCode(201)
                .header("Location", startsWith("/members/"));
    }

    // 2. POST /login (성공)
    @Test
    void 올바른_정보로_로그인하면_200_응답과_쿠키를_발급한다() {
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
    }

    // 3. POST /login (실패)
    @Test
    void 비밀번호가_틀리면_로그인에_실패한다() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "wrong-password");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(400);
    }

    // 4. GET /login/check (성공)
    @Test
    void 로그인_후_발급받은_쿠키로_내_정보를_조회한다() {
        String token = 로그인_토큰_발급("admin@email.com", "password");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(response.body().jsonPath().getString("name")).isEqualTo("어드민");
    }

    // 5. GET /login/check (실패 1: 쿠키 없음)
    @Test
    void 쿠키_없이_내_정보를_조회하면_401을_응답한다() {
        RestAssured.given().log().all()
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }

    // 6. GET /login/check (실패 2: 빈 토큰 쿠키)
    @Test
    void 빈_토큰_쿠키로_내_정보를_조회하면_401을_응답한다() {
        RestAssured.given().log().all()
                .cookie("token", "")
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }

    // 7. POST /logout (성공)
    @Test
    void 로그아웃하면_200_응답과_함께_쿠키가_만료된다() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().post("/logout")
                .then().log().all()
                .statusCode(200)
                .extract();

        String setCookieHeader = response.header("Set-Cookie");
        assertThat(setCookieHeader).contains("Max-Age=0");
    }

    private String 로그인_토큰_발급(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then()
                .statusCode(200)
                .extract();

        return response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
    }
}
