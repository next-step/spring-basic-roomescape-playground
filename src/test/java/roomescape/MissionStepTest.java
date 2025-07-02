package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    /**
     * [추가] 로그인 요청을 보내고 토큰을 반환하는 헬퍼 메서드
     * @param email 로그인할 이메일
     * @param password 로그인할 비밀번호
     * @return 발급된 JWT 토큰
     */
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

        return response.response().getCookie("token");
    }

    @Test
    void 일단계() {
        String token = createToken("admin@email.com", "password");
        assertThat(token).isNotBlank();
    }

    @Test
    void 이단계() {
        String token = createToken("admin@email.com", "password");

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", 1);
        params.put("theme", 1);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).getName()).isEqualTo("어드민");

//        params.put("name", "브라운");
//
//        ExtractableResponse<Response> adminResponse = RestAssured.given().log().all()
//                .body(params)
//                .cookie("token", token)
//                .contentType(ContentType.JSON)
//                .post("/reservations")
//                .then().log().all()
//                .extract();
//
//        assertThat(adminResponse.statusCode()).isEqualTo(201);
//        assertThat(adminResponse.as(ReservationResponse.class).getName()).isEqualTo("브라운");
    }

    @Test
    void 삼단계() {
//        String brownToken = createToken("brown@email.com", "password");
//
//        RestAssured.given().log().all()
//                .cookie("token", brownToken)
//                .get("/admin")
//                .then().log().all()
//                .statusCode(401);

        String adminToken = createToken("admin@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/admin")
                .then().log().all()
                .statusCode(200);
    }
}
