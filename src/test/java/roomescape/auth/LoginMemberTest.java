package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LoginMemberTest {

    @ParameterizedTest
    @ValueSource(strings = {""})
    void 이메일이_유효하지_않은_경우_예외가_발생한다(String email) {
        assertThatThrownBy(() -> new LoginMember(
                email, "어드민", Role.ADMIN
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void 이름이_유효하지_않은_경우_예외가_발생한다(String name) {
        assertThatThrownBy(() -> new LoginMember(
                "admin@email.com", name, Role.ADMIN
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void 역할이_유효하지_않은_경우_예외가_발생한다(String role) {
        assertThatThrownBy(() -> new LoginMember(
                "admin@email.com", "어드민", Role.valueOf(role)
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
