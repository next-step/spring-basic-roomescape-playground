package roomescape.domain.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberTest {

    private final String name = "Alice";
    private final String email = "test@test.com";
    private final String password = "test";
    private final String role = "USER";

    @Test
    void Member는_name이_빈_채로_생성할_수_없다() {

        // name == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member((String) null, email, password, role)
        );

        // name == ""
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member("", email, password, role)
        );

        // name == " "
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(" ", email, password, role)
        );
    }

    @Test
    void Member는_email이_빈_채로_생성할_수_없다() {

        // email == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, null, password, role)
        );

        // email == ""
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, "", password, role)
        );

        // email == " "
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, " ", password, role)
        );
    }

    @Test
    void Member의_email은_이메일_정규식_이외_값을_허용하지_않는다() {

        // email not contains '@'
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, "testtest.com", password, role)
        );

        // email not ends with '.com'
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, "test@test.org", password, role)
        );
    }

    @Test
    void Member는_password가_빈_채로_생성할_수_없다() {

        // password == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, email, null, role)
        );

        // password == ""
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, email, "", role)
        );

        // password == " "
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, email, " ", role)
        );
    }

    @Test
    void Member는_role이_빈_채로_생성할_수_없다() {

        // role == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, email, password, null)
        );

        // email == ""
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, email, password, "")
        );

        // email == " "
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, email, password, " ")
        );
    }

    @Test
    void Member는_role은_USER와_ADMIN_이외의_값을_지정할_수_없다() {

        // role != "USER" && role != "ADMIN"
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Member(name, email, password, "ASDF")
        );
    }

    @Test
    void Member를_정상적으로_생성한_경우() {
        // given
        Member member = new Member(name, email, password, role);

        // then
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(password);
        assertThat(member.getRole()).isEqualTo(role);
    }
}

