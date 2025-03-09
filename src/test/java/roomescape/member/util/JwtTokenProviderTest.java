package roomescape.member.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void 액세스_토큰을_파싱할_수_있다() {
        // given
        Member member = new Member(1L, "멤버", "member@email.com", "USER");
        String accessToken = jwtTokenProvider.createAccessToken(member);
        // when
        long resultOfParseToken = jwtTokenProvider.parseToken(accessToken);
        // then
        assertThat(resultOfParseToken).isEqualTo(member.getId());
    }

    //Todo: 만료된 토큰 및 유효하지 않은 토큰 예외 테스트 추가, but how?
}
