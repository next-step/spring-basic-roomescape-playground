package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.member.Member;
import roomescape.member.MemberDao;


@JdbcTest
@Import(MemberDao.class)
public class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("이메일과 비밀번호로 회원 조회 테스트")
    void findByEmailAndPassword_success() {

        //given
        Member member = new Member("Member", "test@email.com", "password", "USER");
        Member savedMember = memberDao.save(member);

        //when
        Member foundMember = memberDao.findByEmailAndPassword("test@email.com", "password");

        //then
        assertThat(foundMember)
                .usingRecursiveComparison()
                .isEqualTo(savedMember);
    }

    @Test
    @DisplayName("이메일로 회원 조회 테스트")
    void findByEmail_success() {

        //given
        Member member = new Member("Member", "test@email.com", "password", "USER");
        Member savedMember = memberDao.save(member);

        //when
        Member foundMember = memberDao.findByEmail("test@email.com");

        //then
        assertThat(foundMember)
                .usingRecursiveComparison()
                .isEqualTo(savedMember);
    }

    @Test
    @DisplayName("이름으로 회원 조회 테스트")
    void findByName_success() {

        Member member = new Member("Popo", "test@email.com", "password", "USER");
        Member savedMember = memberDao.save(member);

        Member foundMember = memberDao.findByName("Popo");
        assertThat(foundMember)
        .usingRecursiveComparison()
                .isEqualTo(savedMember);

    }

}
