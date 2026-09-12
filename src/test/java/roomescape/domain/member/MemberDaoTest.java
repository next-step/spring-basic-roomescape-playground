package roomescape.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.member.entity.LoginMember;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberDao;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({MemberDao.class})
public class MemberDaoTest {

    private final String name = "Alice";
    private final String email = "test@test.com";
    private final String password = "test";
    private final String role = "USER";

    @Autowired
    private MemberDao memberDao;

    @Test
    void save를_호출하면_ID가_있는_객체를_반환한다() {
        // given
        Member member = new Member(name, email, password, role);

        // when
        Member savedMember = memberDao.save(member);

        // then
        assertThat(member.getId()).isNull();
        assertThat(savedMember.getId()).isNotNull();
    }

    @Test
    void findByEmailAndPassword를_호출하면_일치하는_LoginMember를_반환한다() {
        // schema.sql 시드 회원 (admin@email.com)
        Optional<LoginMember> foundMember = memberDao.findByEmailAndPassword("admin@email.com", "password");

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("어드민");
        assertThat(foundMember.get().getRole()).isEqualTo("ADMIN");
    }

    @Test
    void findLoginMemberById를_호출하면_일치하는_LoginMember를_반환한다() {
        // schema.sql 시드 회원 (id = 1)
        Optional<LoginMember> foundMember = memberDao.findLoginMemberById(1L);

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("어드민");
        assertThat(foundMember.get().getEmail()).isEqualTo("admin@email.com");
    }
}
