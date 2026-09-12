package roomescape.domain.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.domain.auth.principal.LoginMember;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginMemberTest {

    private final Long id = 1L;
    private final String name = "Alice";
    private final String email = "test@test.com";
    private final String role = "USER";

    @Test
    void LoginMember는_id가_빈_채로_생성할_수_없다() {

        // id == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(null, name, email, role)
        );
    }

    @Test
    void LoginMember는_name이_빈_채로_생성할_수_없다() {

        // name == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, null, email, role)
        );

        // name == ""
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, "", email, role)
        );

        // name == " "
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, " ", email, role)
        );
    }

    @Test
    void LoginMember는_email이_빈_채로_생성할_수_없다() {

        // email == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, name, null, role)
        );

        // email == ""
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, name, "", role)
        );

        // email == " "
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, name, " ", role)
        );
    }

    @Test
    void LoginMember의_email은_이메일_정규식_이외_값을_허용하지_않는다() {

        // email not contains '@'
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, name, "testtest.com", role)
        );

        // email not ends with '.com'
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, name, "test@test.org", role)
        );
    }

    @Test
    void LoginMember는_role이_빈_채로_생성할_수_없다() {

        // role == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, name, email, null)
        );

        // role == ""
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, name, email, "")
        );

        // role == " "
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, name, email, " ")
        );
    }

    @Test
    void LoginMember는_role은_USER와_ADMIN_이외의_값을_지정할_수_없다() {

        // role != "USER" && role != "ADMIN"
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LoginMember(id, name, email, "ASDF")
        );
    }

    @Test
    void LoginMember를_정상적으로_생성한_경우() {
        // given
        LoginMember loginMember = new LoginMember(id, name, email, role);

        // then
        assertThat(loginMember.getId()).isEqualTo(id);
        assertThat(loginMember.getName()).isEqualTo(name);
        assertThat(loginMember.getEmail()).isEqualTo(email);
        assertThat(loginMember.getRole()).isEqualTo(role);
    }
}
