package roomescape.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.member.Member;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JwtTokenProviderTest {
    private static final String EMAIL = "email@email.com";
    private static final String NAME = "John Doe";
    private static final String ROLE = "USER";

    @Autowired
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    @Test
    void 토큰_생성() {
        //given
        Member member = new Member(1L, NAME, EMAIL, ROLE);

        //when
        String token = jwtTokenProvider.createToken(member);

        //then
        assertThat(token).isNotBlank();
    }

    @Test
    void 토큰_정보() {
        //given
        Member member = new Member(1L, NAME, EMAIL, ROLE);
        String token = jwtTokenProvider.createToken(member);

        //when
        Map<String, Object> claims = jwtTokenProvider.getClaims(token);

        //then
        assertThat(claims.get("sub")).isEqualTo(EMAIL);
        assertThat(claims.get("name")).isEqualTo(NAME);
        assertThat(claims.get("role")).isEqualTo(ROLE);
    }
}
