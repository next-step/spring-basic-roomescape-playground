package roomescape;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.exception.CreateMemberFailException;
import roomescape.member.controller.MemberController;
import roomescape.member.entity.MemberRequest;
import roomescape.member.entity.MemberResponse;
import roomescape.member.service.MemberService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MemberLogicTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private MemberController memberController;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("아이디_생성_테스트")
    void createMemberTest() {
        // given
        MemberRequest memberRequest = new MemberRequest("Doyo", "member@example.com", "password");

        // when
        ResponseEntity<MemberResponse> response = memberController.createMember(memberRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        MemberResponse responseBody = response.getBody();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getName()).isEqualTo("Doyo");
        assertThat(responseBody.getEmail()).isEqualTo("member@example.com");
    }

    @Test
    @DisplayName("잘못된_이메일_형식_테스트")
    void createMemberWithInvalidEmailTest() {
        // given & when & then
        assertThrows(CreateMemberFailException.class, () -> {
            new MemberRequest("Doyo", "invalid-email", "password");
        });
    }

    @Test
    @DisplayName("빈_패스워드_테스트")
    void createMemberWithEmptyPasswordTest() {
        // given & when & then
        assertThrows(CreateMemberFailException.class, () -> {
            new MemberRequest("Doyo", "member@example.com", "");
        });
    }

    @Disabled
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
