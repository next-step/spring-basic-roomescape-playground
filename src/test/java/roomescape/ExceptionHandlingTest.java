package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.Role;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ExceptionHandlingTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String createToken(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/login")
                        .then().log().all()
                        .statusCode(200)
                        .extract();

        return response.headers()
                .get("Set-Cookie")
                .getValue()
                .split(";")[0]
                .split("=")[1];
    }

    @Test
    void 예약_필수정보가_없으면_400을_응답한다() {
        String token = createToken("admin@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("time", "1");

        RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 존재하지_않는_회원으로_예약하면_404를_응답한다() {
        String token = createToken("admin@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("name", "존재하지 않는 회원");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("time", "1");
        params.put("theme", "1");

        RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 존재하지_않는_시간으로_예약하면_404를_응답한다() {
        String token = createToken("admin@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("time", "9999");
        params.put("theme", "1");

        RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 올바르지_않은_테마를_등록하면_400을_응답한다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", " ");
        params.put("description", "테마 설명");

        RestAssured.given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 존재하지_않는_테마를_삭제하면_404를_응답한다() {
        RestAssured.given().log().all()
                .when().delete("/themes/9999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 존재하지_않는_테마로_예약하면_404를_응답한다() {
        String token = createToken("admin@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("time", "1");
        params.put("theme", "9999");

        RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 같은_날짜와_시간과_테마로_중복_예약하면_409를_응답한다() {
        String token = createToken("admin@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("time", "1");
        params.put("theme", "1");

        RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_404를_응답한다() {
        RestAssured.given().log().all()
                .when().delete("/reservations/9999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 올바르지_않은_시간을_등록하면_400을_응답한다() {
        Map<String, String> params = new HashMap<>();
        params.put("value", " ");

        RestAssured.given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 이미_등록된_시간을_등록하면_409를_응답한다() {
        Map<String, String> params = new HashMap<>();
        params.put("value", "10:00");

        RestAssured.given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void 존재하지_않는_시간을_삭제하면_404를_응답한다() {
        RestAssured.given().log().all()
                .when().delete("/times/9999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 삭제된_시간을_다시_등록하면_복구된다() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);

        Map<String, String> params = new HashMap<>();
        params.put("value", "10:00");

        RestAssured.given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 회원_필수정보가_없으면_400을_응답한다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "새 회원");
        params.put("email", "new@email.com");

        RestAssured.given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/members")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 이미_가입된_이메일로_가입하면_409를_응답한다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "새 회원");
        params.put("email", "admin@email.com");
        params.put("password", "password");

        RestAssured.given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/members")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void 과거_날짜로_예약하면_400을_응답한다() {
        String token = createToken("admin@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", LocalDate.now().minusDays(1).toString());
        params.put("time", "1");
        params.put("theme", "1");

        RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 토큰의_회원이_존재하지_않으면_401을_응답한다() {
        Member nonexistentMember = new Member(
                9999L,
                "존재하지 않는 회원",
                "none@email.com",
                Role.USER
        );

        String token = jwtTokenProvider.createToken(nonexistentMember);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }
    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;
    @Test
    void 만료된_토큰으로_로그인_정보를_조회하면_401을_응답한다() {
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(secretKey, -1000L);

        Member member = new Member(
                1L,
                "어드민",
                "admin@email.com",
                Role.ADMIN
        );

        String expiredToken = expiredTokenProvider.createToken(member);

        RestAssured.given().log().all()
                .cookie("token", expiredToken)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }
}
