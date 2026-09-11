package roomescape.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.exception.MemberException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import({MemberService.class, MemberDao.class})
class MemberServiceTest {
    private static final String EMAIL = "login-service@email.com";
    private static final String PASSWORD = "password";
    private Long memberId;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into member (name, email, password, role) values (?, ?, ?, ?)",
                "테스터", EMAIL, PASSWORD, "USER");
        memberId = jdbcTemplate.queryForObject("select id from member where email = ?", Long.class, EMAIL);
    }

    @Test
    void 로그인_정보가_일치하면_회원_ID를_반환한다() {
        // when
        Long result = memberService.login(EMAIL, PASSWORD);

        // then
        assertThat(result).isEqualTo(memberId);
    }

    @Test
    void 일치하는_회원이_없으면_로그인_실패_예외를_던진다() {
        // given
        String wrongPassword = "wrong-password";

        // when & then
        assertThatThrownBy(() -> memberService.login(EMAIL, wrongPassword))
                .isInstanceOfSatisfying(MemberException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.LOGIN_FAILED));
    }

}
