package roomescape.auth;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import roomescape.member.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
    private final Member member = new Member(1L, "어드민", "admin@email.com", "ADMIN");

    @Test
    void 유효한_토큰이면_로그인_정보를_반환한다() {
        String token = jwtTokenProvider.createToken(member);
        LoginMember loginMember = jwtTokenProvider.getLoginMember(token);

        assertThat(loginMember.getName()).isEqualTo("어드민");
    }

    @Test
    void 만료된_토큰이면_예외가_발생한다() {
        String expiredToken = jwtTokenProvider.createToken(member, -1000);

        assertThatThrownBy(() -> jwtTokenProvider.getLoginMember(expiredToken))
                .isInstanceOf(ExpiredJwtException.class);
    }
}
