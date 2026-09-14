package roomescape.domain.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.auth.repository.AuthDao;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({AuthDao.class})
public class AuthDaoTest {

    @Autowired
    private AuthDao authDao;

    @Test
    void findByEmailAndPassword를_호출하면_일치하는_LoginMember를_반환한다() {
        // when
        Optional<LoginMember> foundMember = authDao.findByEmailAndPassword("admin@email.com", "password");

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("어드민");
        assertThat(foundMember.get().getRole()).isEqualTo("ADMIN");
    }

    @Test
    void findByEmailAndPassword에_일치하는_회원이_없으면_빈_Optional을_반환한다() {
        // when
        Optional<LoginMember> foundMember = authDao.findByEmailAndPassword("admin@email.com", "wrong-password");

        // then
        assertThat(foundMember).isEmpty();
    }
}
