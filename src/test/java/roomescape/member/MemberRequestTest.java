package roomescape.member;

import org.junit.jupiter.api.Test;
import roomescape.exception.RoomEscapeException;

import static org.assertj.core.api.Assertions.*;

class MemberRequestTest {

    @Test
    void 정상값이면_객체_생성된다(){
        //given
        MemberRequest request = new MemberRequest("석준", "aaa@naver.com", "123");

        //then
        assertThat(request.name()).isEqualTo("석준");
        assertThat(request.email()).isEqualTo("aaa@naver.com");
        assertThat(request.password()).isEqualTo("123");
    }

    @Test
    void 이름이_비면_예외를_던진다(){
        assertThatThrownBy(()-> new MemberRequest("  ", "aaa@naver.com", "123"))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이름은 필수입니다");
    }

    @Test
    void 이메일_형식이_틀리면_예외를_던진다(){
        assertThatThrownBy(()-> new MemberRequest("석준", "badEmail", "123"))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이메일 형식이 맞지 않습니다");
    }

    @Test
    void 비밀번호가_비면_예외를_던진다(){
        assertThatThrownBy(()-> new MemberRequest("석준", "aaa@naver.com", null))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("비밀번호는 필수입니다");
    }



}
