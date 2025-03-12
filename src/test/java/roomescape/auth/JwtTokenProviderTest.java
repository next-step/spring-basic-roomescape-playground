package roomescape.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.DataBaseCleaner;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(DataBaseCleaner.class)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void 액세스_토큰을_파싱할_수_있다() {
        // given
        Member member = new Member(1L, "멤버", "member@email.com", Role.USER);
        String accessToken = jwtTokenProvider.createAccessToken(member);
        // when
        long resultOfParseToken = jwtTokenProvider.parseToken(accessToken);
        // then
        assertThat(resultOfParseToken).isEqualTo(member.getId());
    }

    //Todo: 만료된 토큰 및 유효하지 않은 토큰 예외 테스트 추가, but how?
}
