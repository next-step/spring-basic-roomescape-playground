package roomescape.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.MemberAuthContext;
import roomescape.auth.MemberCredential;
import roomescape.member.Member;
import roomescape.member.MemberFixture;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void create_token() {
        String memberName = "test";
        Member member = MemberFixture.memberWithName(memberName);
        MemberAuthContext authContext = new MemberAuthContext(member.getName(), member.getRole());

        MemberCredential token = jwtProvider.create(authContext);

        assertThat(token.authorization()).isNotNull();
    }

    @Test
    void parse_token() {
        String memberName = "test";
        Member member = MemberFixture.memberWithName(memberName);
        MemberAuthContext authContext = new MemberAuthContext(member.getName(), member.getRole());
        MemberCredential token = jwtProvider.create(authContext);

        MemberAuthContext actual = jwtProvider.parseCredential(token);

        Assertions.assertThat(actual).isEqualTo(authContext);
    }
}
