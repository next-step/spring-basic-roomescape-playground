package roomescape.member.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 아이디를_통해_멤버를_조회한다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        Member savedMember = memberRepository.save(member);
        // when
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());
        // then
        assertThat(foundMember)
                .hasValueSatisfying(memberResult -> assertAll(
                        () -> assertThat(memberResult.getName()).isEqualTo(savedMember.getName()),
                        () -> assertThat(memberResult.getEmail()).isEqualTo(savedMember.getEmail()),
                        () -> assertThat(memberResult.getRole()).isEqualTo(savedMember.getRole())
                ));
    }

    @Test
    void 특정_아이디를_가진_멤버가_없을시_빈_값을_반환한다() {
        // given &  when
        Optional<Member> foundMember = memberRepository.findById(0L);
        // then
        assertThat(foundMember).isEmpty();
    }

    @Test
    void 이메일_및_비밀번호를_통해_멤버를_조회한다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        Member savedMember = memberRepository.save(member);
        // when
        Optional<Member> foundMember = memberRepository.findByEmailAndPassword(member.getEmail(), member.getPassword());
        // then
        assertThat(foundMember)
                .hasValueSatisfying(memberResult -> assertAll(
                        () -> assertThat(memberResult.getName()).isEqualTo(savedMember.getName()),
                        () -> assertThat(memberResult.getEmail()).isEqualTo(savedMember.getEmail()),
                        () -> assertThat(memberResult.getRole()).isEqualTo(savedMember.getRole())
                ));
    }

    @Test
    void 이메일_또는_비밀번호가_일치하지_않으면_빈_값을_반환한다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        memberRepository.save(member);
        // when
        Optional<Member> foundMemberWithWrongEmail = memberRepository.findByEmailAndPassword("wrong@email.com", member.getPassword());
        Optional<Member> foundMemberWithWrongPassword = memberRepository.findByEmailAndPassword(member.getEmail(), "wrongPassword");
        // then
        assertAll(
                () -> assertThat(foundMemberWithWrongEmail).isEmpty(),
                () -> assertThat(foundMemberWithWrongPassword).isEmpty()
        );
    }
}
