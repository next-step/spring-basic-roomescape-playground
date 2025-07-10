package roomescape.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.RoomEscapeException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("아이디 또는 비밀번호가 틀리면 예외를 던진다")
    void 아이디_또는_비밀번호가_틀리면_예외를_던진다() {
        //given
        String badEmail = "badEmail";
        String badPassword = "badPassword";

        //then
        assertThatThrownBy(() -> memberService.authenticate(badEmail, badPassword))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이메일이 잘못되었습니다.");
    }

    @Test
    @DisplayName("아이디 또는 비밀번호가 맞으면 정상 흐름")
    void 아이디_또는_비밀번호가_맞으면_정상_흐름() {
        //given
        String goodEmail = "admin@email.com";
        String goodPassword = "password";

        //when
        Member member = memberService.authenticate(goodEmail, goodPassword);

        //then
        assertThat(member.getName()).isEqualTo("어드민");
    }

    @Test
    @DisplayName("존재하지 않는 id로 member를 조회하면 예외를 던진다")
    void 존재하지_않는_id로_member를_조회하면_예외를_던진다() {
        //given
        Long memberId = 99999L;

        //then
        assertThatThrownBy(() -> memberService.getById(memberId))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("중복된 이메일로 가입을 하면 예외를 던진다")
    void 중복된_이메일로_가입을_하면_예외를_던진다() {
        //given
        MemberRequest memberRequest1 = new MemberRequest("석준","abc@naver.com","123");
        MemberRequest memberRequest2 = new MemberRequest("석준","abc@naver.com","123");
        memberService.createMember(memberRequest1);

        //then
        assertThatThrownBy(() -> memberService.createMember(memberRequest2))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이미 가입된 이메일입니다.");
    }

    @Test
    @DisplayName("비밀번호가 틀리면 예외를 던진다")
    void 비밀번호가_틀리면_예외를_던진다() {
        //given
        String goodEmail = "admin@email.com";
        String badPassword = "badPassword";

        //then
        assertThatThrownBy(() -> memberService.authenticate(goodEmail, badPassword))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("비밀번호가 틀렸습니다.");
    }
}
