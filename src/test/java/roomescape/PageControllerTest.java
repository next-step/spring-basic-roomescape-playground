package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.request.LoginRequest;
import roomescape.member.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PageControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setupRestAssured() {
        RestAssured.port = port;
    }

    @Test
    void 관리자_역할을_지닌_유저는_관리자_페이지에_접근할_수_있다() {
        Member member = new Member("관리자1", "admin1@email.com", "password", Role.ADMIN);
        Member savedMember = memberRepository.save(member);

        LoginRequest loginRequest = new LoginRequest(savedMember.getEmail(), savedMember.getPassword());
        String accessToken = createToken(loginRequest);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 접근권한이_없는_유저가_관리자_페이지_접근시_예외가_발생한다() {
        Member member = new Member("멤버1", "member@email.com", "password", Role.USER);
        Member savedMember = memberRepository.save(member);

        LoginRequest loginRequest = new LoginRequest(savedMember.getEmail(), savedMember.getPassword());
        String accessToken = createToken(loginRequest);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 로그인을_안한_유저가_관리자_페이지_접근시_예외가_발생한다() {
        Member member = new Member("멤버1", "member@email.com", "password", Role.USER);
        Member savedMember = memberRepository.save(member);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", null)
                .when().get("/admin")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    private String createToken(LoginRequest request) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        return response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
    }
}
