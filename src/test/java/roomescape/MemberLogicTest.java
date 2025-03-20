package roomescape;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.CreateMemberFailException;
import roomescape.member.dto.MemberRequest;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberLogicTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("아이디_생성_테스트")
    void createMemberTest() {
        // given
        Map<String, String> memberRequest = new HashMap<>();
        memberRequest.put("name", "Doyo");
        memberRequest.put("email", "member@example.com");
        memberRequest.put("password", "password");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(memberRequest)
                .contentType(ContentType.JSON)
                .post("/members")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("name")).isEqualTo("Doyo");
        assertThat(response.jsonPath().getString("email")).isEqualTo("member@example.com");
    }

    @Test
    @DisplayName("잘못된_이메일_형식으로_아이디를_생성할때_실패한다")
    void createMemberWithInvalidEmailTest() {
        // given & when & then
        assertThrows(CreateMemberFailException.class, () -> {
            new MemberRequest("Doyo", "invalid-email", "password");
        });
    }

    @Test
    @DisplayName("빈_패스워드로_아이디를_생성할때_예외가_발생한다")
    void createMemberWithEmptyPasswordTest() {
        // given & when & then
        assertThrows(CreateMemberFailException.class, () ->
                new MemberRequest("Doyo", "member@example.com", ""));
    }

    @Test
    @DisplayName("잘못된_비밀번호_로그인_테스트")
    void loginWithIncorrectPasswordTest() {
        String email = "admin@email.com";
        String incorrectPassword = "wrongpassword";

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", email);
        loginRequest.put("password", incorrectPassword);

        // when & then
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .post("/login")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(401);
    }
}
