package roomescape.member;

import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @Test
    void 이름이_비어있으면_예외가_발생한다() {
        // given
        String invalidName = "";

        // when & then
        assertThatThrownBy(() -> new Member(invalidName, "admin@email.com", "password", "USER"))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");
    }

    @Test
    void 이메일_형식이_올바르지_않으면_예외가_발생한다() {
        // given
        String invalidEmail = "invalid-email";

        // when & then
        assertThatThrownBy(() -> new Member("어드민", invalidEmail, "password", "USER"))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
    }
}
