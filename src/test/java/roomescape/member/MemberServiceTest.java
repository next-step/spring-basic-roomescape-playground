package roomescape.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
        // given
        LoginRequest request = new LoginRequest(EMAIL, PASSWORD);

        // when
        Long result = memberService.login(request);

        // then
        assertThat(result).isEqualTo(memberId);
    }

    @ParameterizedTest
    @CsvSource({"login-service@email.com, wrong", "unknown@email.com, password", "unknown@email.com, wrong"})
    void 로그인_정보가_일치하지_않으면_동일한_로그인_실패_예외를_던진다(String email, String password) {
        // given
        LoginRequest request = new LoginRequest(email, password);

        // when & then
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOfSatisfying(MemberException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.LOGIN_FAILED));
    }

    @Test
    void 회원_ID로_조회한_회원_객체를_반환한다() {
        // when
        Member member = memberService.getMember(memberId);

        // then
        assertThat(member.getId()).isEqualTo(memberId);
        assertThat(member.getName()).isEqualTo("테스터");
        assertThat(member.getEmail()).isEqualTo(EMAIL);
        assertThat(member.getRole()).isEqualTo("USER");
    }
}
