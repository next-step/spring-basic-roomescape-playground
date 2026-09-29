package roomescape.domain.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.auth.repository.AuthRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuthRepositoryTest {

    @Autowired
    private AuthRepository authRepository;

    @Test
    void findByEmailAndPassword를_호출하면_일치하는_LoginMember를_반환한다() {
        // when
        Optional<LoginMember> foundMember = authRepository.findByEmailAndPassword("admin@dummy.com", "dummy");

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().name()).isEqualTo("더미_어드민");
        assertThat(foundMember.get().role()).isEqualTo("ADMIN");
    }

    @Test
    void findByEmailAndPassword에_일치하는_회원이_없으면_빈_Optional을_반환한다() {
        // when
        Optional<LoginMember> foundMember = authRepository.findByEmailAndPassword("admin@dummy.com", "wrong-password");

        // then
        assertThat(foundMember).isEmpty();
    }
}
