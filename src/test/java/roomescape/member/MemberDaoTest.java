package roomescape.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(MemberDao.class)
class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    private Member savedMember;

    @BeforeEach
    void setUp() {
        Member newMember = new Member("테스트유저", "dao-test@email.com", "password", "USER");
        savedMember = memberDao.save(newMember);
    }

    @Test
    void 회원을_정상적으로_저장한다() {
        // given
        Member member = new Member("테스터", "test@email.com", "1234", "USER");

        // when
        Member result = memberDao.save(member);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("테스터");
    }

    @Test
    void 이메일과_비밀번호가_일치하면_회원을_조회한다() {
        // when
        Member member = memberDao.findByEmailAndPassword(savedMember.getEmail(), "password");

        // then
        assertThat(member.getName()).isEqualTo(savedMember.getName());
        assertThat(member.getName()).isEqualTo(savedMember.getName());
    }

    @Test
    void 일치하는_이메일과_비밀번호가_없으면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> memberDao.findByEmailAndPassword(savedMember.getEmail(), "wrong-password"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void ID로_회원을_조회한다() {
        // when
        Member member = memberDao.findById(savedMember.getId());

        // then
        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(member.getRole()).isEqualTo("USER");
    }

    @Test
    void 존재하지_않는_ID로_조회하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> memberDao.findById(999L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void 이름으로_회원을_조회한다() {
        // when
        Member member = memberDao.findByName(savedMember.getName());

        // then
        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(member.getRole()).isEqualTo("USER");
    }
}
