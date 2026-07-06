package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.JwtTokenProvider;
import roomescape.member.Role;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionOneTest {

    @LocalServerPort
    private int port;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Test
    void 로그인하면_토큰_쿠키가_발급된다() {
        ExtractableResponse<Response> response = loginAsAdmin();

        String token = response.cookie("token");

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void 토큰의_유효_시간이_지나기_전에는_인증되고_지난_후에는_인증에_실패한다() throws InterruptedException {
        long validityInMilliseconds = 2_000L;
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, validityInMilliseconds);
        String token = jwtTokenProvider.createToken("1", "어드민", "admin@email.com", Role.ADMIN);

        RestAssured.given().log().all()
                .port(port)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200);

        Thread.sleep(validityInMilliseconds + 1_000L);

        RestAssured.given().log().all()
                .port(port)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    void 토큰으로_로그인_회원의_이름을_추출한다() {
        String token = loginAsAdmin().cookie("token");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .port(port)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(response.jsonPath().getString("name")).isEqualTo("어드민");
        assertThat(response.jsonPath().getString("email")).isEqualTo("admin@email.com");
    }

    @Test
    void 로그인_정보가_틀리면_인증에_실패한다() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "wrong-password");

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }

    private ExtractableResponse<Response> loginAsAdmin() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "password");

        return RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();
    }

}
