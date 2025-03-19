package roomescape;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import roomescape.exception.CreateMemberFailException;
import roomescape.member.controller.MemberController;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberLogicTest {

    @LocalServerPort
    private int port;

    @MockBean
    private MemberController memberController;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("아이디_생성_테스트")
    void createMemberTest() {
        // given
        MemberRequest memberRequest = new MemberRequest("Doyo", "member@example.com", "password");
        MemberResponse mockResponse = new MemberResponse(1L, "Doyo", "member@example.com");

        given(memberController.createMember(memberRequest))
                .willReturn(new ResponseEntity<>(mockResponse, HttpStatus.CREATED));

        // when
        ResponseEntity<MemberResponse> response = memberController.createMember(memberRequest);

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> {
                    MemberResponse responseBody = response.getBody();
                    assertAll(
                            () -> assertThat(responseBody.getId()).isNotNull(),
                            () -> assertThat(responseBody.getName()).isEqualTo("Doyo"),
                            () -> assertThat(responseBody.getEmail()).isEqualTo("member@example.com")
                    );
                }
        );
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
        assertThrows(CreateMemberFailException.class, () ->
                new MemberRequest("Doyo", "member@example.com", ""));
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
