package roomescape.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("findByEmailAndPassword : 저장한 Member Email 과 Password 조회 시 성공한다")
    @Test
    void given_new_member_when_findByEmailAndPassword_then_return() {
        //given
        String username = "gomgom";
        String email = "gome@email.com";
        String password = "1234";
        Member member = Member.ofUser(username, email, password);
        memberRepository.save(member);
        // when
        Member foundMember = memberRepository.findByEmailAndPassword(email, password)
                .orElse(null);
        // then
        assertAll(
                () -> assertThat(foundMember).isNotNull(),
                () -> assertThat(foundMember.getName()).isEqualTo(username),
                () -> assertThat(foundMember.getEmail()).isEqualTo(email),
                () -> assertThat(foundMember.getPassword()).isEqualTo(password)
        );
    }

    @DisplayName("findByName : 저장한 Member Name 조회 시 성공한다")
    @Test
    void given_save_member_when_findByName_then_return() {
        // given
        String username = "gomgom";
        Member member = Member.ofUser(username, "gome@email.com", "1234");
        memberRepository.save(member);
        // when
        Member foundmember = memberRepository.findByName(username)
                .orElse(null);
        // then
        assertAll(
                () -> assertThat(foundmember).isNotNull(),
                () -> assertThat(foundmember.getName()).isEqualTo(username)
        );
    }

    @DisplayName("existsByEmail : 저장한 Member Email 조회 시 true를 반환한다")
    @Test
    void given_savedMember_when_existsByEmail_then_return_true() {
        // given
        String username = "gomgom";
        String email = "gome@email.com";
        Member member = Member.ofUser(username, email, "1234");
        memberRepository.save(member);
        // when
        boolean result = memberRepository.existsByEmail(email);
        // then
        assertThat(result).isTrue();
    }
}
