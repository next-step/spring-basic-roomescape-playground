package roomescape.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.member.Member;

class JwtTokenProviderTest {
    private JwtProperties jwtProperties;
    private Member member;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties("ThisIsATestKeyForJsonWebTokenProvider", 6000);
        member = new Member(1L, "test", "test@email.com", "ADMIN");
    }

    @Test
    void 토큰_생성() {
        //given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);

        //when
        String token = jwtTokenProvider.createToken(member);

        //then
        assertThat(token).isNotBlank();
    }

    @Test
    void 토큰_정보() {
        //given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);
        String token = jwtTokenProvider.createToken(member);

        //when
        Map<String, Object> claims = jwtTokenProvider.getClaims(token);

        //then
        assertThat(claims.get("sub")).isEqualTo(member.getEmail());
        assertThat(claims.get("name")).isEqualTo(member.getName());
        assertThat(claims.get("role")).isEqualTo(member.getRole());
    }
}
