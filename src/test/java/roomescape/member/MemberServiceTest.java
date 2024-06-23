package roomescape.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.MemberAuthContext;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void create_member() {
        Member member = MemberFixture.memberWithName("name");
        MemberRequest request = new MemberRequest(
                member.getName(),
                member.getEmail(),
                member.getPassword()
        );

        assertThatCode(() -> memberService.createMember(request)).doesNotThrowAnyException();
    }


    @Test
    void login_by_email_and_password() {
        Member member = MemberFixture.memberWithName("name");
        MemberRequest saveRequest = new MemberRequest(
                member.getName(),
                member.getEmail(),
                member.getPassword()
        );
        memberService.createMember(saveRequest);
        MemberLoginRequest loginRequest = new MemberLoginRequest(
                member.getEmail(),
                member.getPassword()
        );

        MemberAuthContext loginMember = memberService.loginByEmailAndPassword(loginRequest);

        assertThat(loginMember.name()).isEqualTo(member.getName());
    }

    @Test
    void throw_when_try_to_login_with_invalid_info() {
        MemberLoginRequest loginRequest = new MemberLoginRequest(
                "email",
                "password"
        );

        assertThatThrownBy(() -> memberService.loginByEmailAndPassword(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로그인 정보가 불일치 합니다.");
    }
}
