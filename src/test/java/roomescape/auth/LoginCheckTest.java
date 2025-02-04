package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginCheckTest {

    @Test
    void 인증_정보_조회_성공() {
        String token = createToken("admin@email.com", "password");

        LoginMember loginMember = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract().as(LoginMember.class);

        assertThat(loginMember.name()).isEqualTo("어드민");
        assertThat(loginMember.email()).isEqualTo("admin@email.com");
        assertThat(loginMember.role()).isEqualTo(Role.ADMIN);
    }

    @Test
    void 인증_토큰_쿠키가_비어있는_경우_인증_정보_조회에_실패한다() {
        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/login/check")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(400);
    }

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

        return response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
    }
}
