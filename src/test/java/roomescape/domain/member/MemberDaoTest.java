package roomescape.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberDao;

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
}
