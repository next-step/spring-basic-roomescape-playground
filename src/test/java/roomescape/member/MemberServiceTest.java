package roomescape.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.AuthenticationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository, jwtTokenProvider);
    }

    @Test
    void 회원을_정상적으로_생성한다() {
        // given
        MemberRequest request = new MemberRequest();
        Member savedMember = new Member(1L, "어드민", "admin@email.com", "USER");
        given(memberRepository.save(any(Member.class))).willReturn(savedMember);

        // when
        MemberResponse response = memberService.createMember(request);

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("어드민");
    }

    @Test
    void 이메일과_비밀번호가_일치하면_토큰을_발급한다() {
        // given
        String email = "admin@email.com";
        String password = "password";
        Member member = new Member(1L, "어드민", email, "ADMIN");

        given(memberRepository.findByEmailAndPassword(email, password)).willReturn(Optional.of(member));
        given(jwtTokenProvider.createToken(member)).willReturn("mocked-jwt-token");

        // when
        String token = memberService.login(email, password);

        // then
        assertThat(token).isEqualTo("mocked-jwt-token");
    }

    @Test
    void 로그인_정보가_일치하지_않으면_AuthenticationException이_발생한다() {
        // given
        String wrongEmail = "wrong@email.com";
        String wrongPassword = "wrong-password";
        given(memberRepository.findByEmailAndPassword(anyString(), anyString()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.login(wrongEmail, wrongPassword))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    @Test
    void 토큰으로_회원_정보를_조회한다() {
        // given
        String token = "valid-jwt-token";
        Member member = new Member(1L, "어드민", "admin@email.com", "ADMIN");

        given(jwtTokenProvider.getMemberId(token)).willReturn(1L);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        // when
        Member response = memberService.findMemberByToken(token);

        // then
        assertThat(response.getName()).isEqualTo("어드민");
        assertThat(response.getEmail()).isEqualTo("admin@email.com");
        assertThat(response.getRole()).isEqualTo("ADMIN");
    }
}
