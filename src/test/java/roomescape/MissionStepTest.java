package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.jwt.TokenProvider;
import roomescape.member.Member;
import roomescape.reservation.MyReservationResponse;
import roomescape.reservation.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MissionStepTest {

    @Autowired
    private TokenProvider tokenProvider;

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

        return response.headers().get("Set-Cookie")
            .getValue()
            .split(";")[0]
            .split("=")[1];
    }

    @Test
    void 일단계() {
        String token = createToken("admin@email.com", "password");
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("로그인 후 쿠키로 사용자정보 조회 가능")
    void Login_After_Cookie_Inquire_User_Information_Available() {
        String token = createToken("admin@email.com", "password");

        // 사용자 정보 확인
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
        String token = createToken("admin@email.com",
            "password");  // 일단계에서 토큰을 추출하는 로직을 메서드로 따로 만들어서 활용하세요.

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
    @DisplayName("잘못된 형식의 토큰일 때 401 Unauthorized 응답을 반환한다")
    void Invalid_Token_Format_Return_401() {
        String invalidToken = "not.JWT.Token";

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .cookie("token", invalidToken)
            .when().get("/login/check")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    @DisplayName("정상적인 토큰은 200 OK 응답을 반환한다")
    void Valid_Token_Returns_200() {
        String validToken = createToken("admin@email.com", "password");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .cookie("token", validToken)
            .when().get("/login/check")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("서명이 위조된 토큰은 401 Unauthrized를 반환한다")
    void Tampered_Token_Returns_401() {
        String validToken = createToken("admin@email.com", "password");
        String tamperedToken = validToken.substring(0,
            validToken.lastIndexOf('.') + 1) + "invalidsignature";

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .cookie("token", tamperedToken)
            .when().get("/login/check")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    @DisplayName("구조만 맞고 base64 디코딩이 안 되는 토큰은 401을 반환한다")
    void Invalid_Base64_Token_Returns_401() {
        String invalidBase64Token = "aW52YWxpZC5iYXNlNjQhIT8=.cGF5bG9hZA==.c2lnbmF0dXJl!"; //base64 디코딩이 안되는 토큰

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .cookie("token", invalidBase64Token)
            .when().get("/login/check")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    @DisplayName("빈 토큰은 401 Unauthorized를 반환한다.")
    void Empty_Token_Returns_401() {
        ExtractableResponse response = RestAssured.given().log().all()
            .cookie("token", "")
            .when().get("/login/check")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    @DisplayName("토큰 생성 후 파싱 시 로그인 정보가 추출된다")
    void createAndParseToken() {
        Member member = new Member(1L, "어드민", "admin@email.com", "ADMIN");

        String token = tokenProvider.createToken(member);
        LoginMember loginMember = tokenProvider.parseLoginMember(token);

        assertThat(loginMember.id()).isEqualTo(member.getId());
        assertThat(loginMember.name()).isEqualTo(member.getName());
        assertThat(loginMember.role()).isEqualTo(member.getRole());
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
}
