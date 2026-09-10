package roomescape.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
    }

    @Test
    void 회원의_토큰을_정상적으로_발급한다() {
        // given
        Member member = new Member(1L, "어드민", "admin@email.com", "ADMIN");

        // when
        String token = jwtTokenProvider.createToken(member);

        // then
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void 정상적인_토큰에서_회원_ID를_추출한다() {
        // given
        Member member = new Member(1L, "어드민", "admin@email.com", "ADMIN");
        String token = jwtTokenProvider.createToken(member);

        // when
        Long extractedId = jwtTokenProvider.getMemberId(token);

        // then
        assertThat(extractedId).isEqualTo(member.getId());
    }

    @Test
    void 변조된_토큰을_전달하면_예외가_발생한다() {
        // given
        String invalidToken = "invalid.jwt.token";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.getMemberId(invalidToken))
                .isInstanceOf(Exception.class);
    }
}
