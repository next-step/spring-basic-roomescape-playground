package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import roomescape.member.Role;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 정보 저장 및 조회 테스트")
    void saveAndFindMember() {
        //given
        Member member = new Member("빵빵이", "bbangbbang@naver.com", "qwer", Role.USER);
        memberRepository.save(member);

        //when
        Optional<Member> foundMember = memberRepository.findByEmailAndName("bbangbbang@naver.com", "빵빵이");

        //then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("빵빵이");
        assertThat(foundMember.get().getEmail()).isEqualTo("bbangbbang@naver.com");
    }
}
