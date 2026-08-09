package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.auth.repository.RefreshTokenRepository;
import roomescape.config.DatabaseCleanerExtension;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.support.querycounter.QueryCounterTestConfig;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.repository.WaitingRepository;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@ExtendWith(DatabaseCleanerExtension.class)
@Import(QueryCounterTestConfig.class)
@SuppressWarnings("NonAsciiCharacters")
public class ApiTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ReservationRepository reservationRepository;

    @Autowired
    protected TimeRepository timeRepository;

    @Autowired
    protected ThemeRepository themeRepository;

    @Autowired
    protected WaitingRepository waitingRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    protected ExtractableResponse<Response> 로그인_시도(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .extract();
    }
}
