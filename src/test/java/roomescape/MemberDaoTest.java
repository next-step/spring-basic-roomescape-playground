package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.member.domain.Member;
import roomescape.member.dao.MemberDao;


@JdbcTest
@Import(MemberDao.class)
public class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("멤버_생성_조회_테스트")
    void saveMember_Test() {
        // Given
        Member member = new Member("Brown", "Brown@example.com", "password", "USER");

        // When
        Member savedMember = memberDao.save(member);

        // Then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getName()).isEqualTo("Brown");
        assertThat(savedMember.getEmail()).isEqualTo("Brown@example.com");
        assertThat(savedMember.getRole()).isEqualTo("USER");
        assertThat(savedMember.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("이메일과_비밀번호로_회원_조회_테스트")
    void findByEmailAndPassword_Test() {

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
    @DisplayName("아이디로_회원_조회_테스트")
    void findById_Test() {

        //given
        Member member = new Member("Member", "test@email.com", "password", "USER");
        Member savedMember = memberDao.save(member);

        //when
        Member foundMember = memberDao.findById(savedMember.getId());

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember)
                .usingRecursiveComparison()
                .isEqualTo(savedMember);
    }

    @Test
    @DisplayName("이름으로_회원_조회_테스트")
    void findByName_Test() {

        Member member = new Member("Popo", "test@email.com", "password", "USER");
        Member savedMember = memberDao.save(member);

        Member foundMember = memberDao.findByName("Popo");
        assertThat(foundMember)
        .usingRecursiveComparison()
                .isEqualTo(savedMember);
    }

}
