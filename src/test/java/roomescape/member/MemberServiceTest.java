package roomescape.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.exception.RoomEscapeException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
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
                .hasMessage("이메일 또는 비밀번호가 잘못되었습니다.");
    }

    @Test
    @DisplayName("아이디_또는_비밀번호가_맞으면_정상_흐름")
    void 아이디_또는_비밀번호가_맞으면_정상_흐름() {
        //given
        String goodEmail = "admin@email.com";
        String goodPassword = "password";

        //when
        Member member = memberService.authenticate(goodEmail, goodPassword);

        //then
        assertThat(member.getName()).isEqualTo("어드민");

    }
}
