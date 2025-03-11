package roomescape.member.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Import(MemberDao.class)
class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void 아이디를_통해_멤버를_조회한다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        Member savedMember = memberDao.save(member);
        // when
        Optional<Member> foundMember = memberDao.findById(savedMember.getId());
        // then
        assertThat(foundMember).isPresent()
                .get()
                .extracting("name", "email", "role")
                .containsExactlyInAnyOrder("멤버", "member@email.com",  Role.USER);
    }

    @Test
    void 특정_아이디를_가진_멤버가_없을시_빈_값을_반환한다() {
        // given &  when
        Optional<Member> foundMember = memberDao.findById(0L);
        // then
        assertThat(foundMember).isEmpty();
    }

    @Test
    void 이메일_및_비밀번호를_통해_멤버를_조회한다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        memberDao.save(member);
        // when
        Optional<Member> foundMember = memberDao.findByEmailAndPassword(member.getEmail(), member.getPassword());
        // then
        assertThat(foundMember).isPresent()
                .get()
                .extracting("name", "email", "role")
                .containsExactlyInAnyOrder("멤버", "member@email.com",  Role.USER);
    }

    @Test
    void 이메일_또는_비밀번호가_일치하지_않으면_빈_값을_반환한다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        memberDao.save(member);
        // when
        Optional<Member> foundMemberWithWrongEmail = memberDao.findByEmailAndPassword("wrong@email.com", member.getPassword());
        Optional<Member> foundMemberWithWrongPassword = memberDao.findByEmailAndPassword(member.getEmail(), "wrongPassword");
        // then
        assertAll(
                () -> assertThat(foundMemberWithWrongEmail).isEmpty(),
                () -> assertThat(foundMemberWithWrongPassword).isEmpty()
        );
    }
}
