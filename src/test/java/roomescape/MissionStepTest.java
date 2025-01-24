package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.AuthService;
import roomescape.auth.LoginRequestDto;
import roomescape.auth.TokenGenerator;
import roomescape.member.Member;
import roomescape.reservation.ReservationResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    private final TokenGenerator tokenGenerator;

    public MissionStepTest(
            @Autowired TokenGenerator tokenGenerator
    ) {
        this.tokenGenerator = tokenGenerator;
    }

    @Test
    void 일단계() {
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

        ExtractableResponse<Response> checkResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(checkResponse.body().jsonPath().getString("name")).isEqualTo("어드민");
    }

    @Nested
    class 토큰_생성_포함_테스트 {
        @Autowired AuthService authService;

        @Test
        void 이단계() {
            LoginRequestDto loginRequestDto = new LoginRequestDto("admin@email.com", "password");
            Member member = authService.loginByEmailAndPassword(loginRequestDto);
            String token = tokenGenerator.generateAccessToken(member);

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
            LoginRequestDto brwonLoginRequestDto = new LoginRequestDto("brown@email.com", "password");
            Member brownMember = authService.loginByEmailAndPassword(brwonLoginRequestDto);
            String brownToken = tokenGenerator.generateAccessToken(brownMember);

            RestAssured.given().log().all()
                    .cookie("token", brownToken)
                    .get("/admin")
                    .then().log().all()
                    .statusCode(401);

            LoginRequestDto adminLoginRequestDto = new LoginRequestDto("admin@email.com", "password");
            Member adminMember = authService.loginByEmailAndPassword(adminLoginRequestDto);
            String adminToken = tokenGenerator.generateAccessToken(adminMember);

            RestAssured.given().log().all()
                    .cookie("token", adminToken)
                    .get("/admin")
                    .then().log().all()
                    .statusCode(200);
        }
    }
}
