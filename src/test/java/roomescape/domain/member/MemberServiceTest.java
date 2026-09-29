package roomescape.domain.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.global.exception.ConflictException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(MemberService.class)
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    private final String name = "Alice";
    private final String email = "dummy@dummy.com";
    private final String password = "dummy";

    @Test
    void 이미_저장된_email로_createMember를_호출하면_409_예외를_던진다() {
        // data-test.sql
        Assertions.assertThrows(
                ConflictException.class,
                () -> memberService.createMember(name, "user@dummy.com", password)
        );
    }

    @Test
    void 정상적으로_createMember_메소드가_실행됨() {
        // when
        Member member = memberService.createMember(name, email, password);

        // then
        assertThat(member.getId()).isNotNull();
        assertThat(member.getNickname()).isEqualTo(name);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(password);
        assertThat(member.getRole()).isEqualTo("USER");
    }
}
