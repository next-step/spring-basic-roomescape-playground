package roomescape.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class MemberDaoTest {
    private static final String EMAIL = "login-dao@email.com";
    private static final String PASSWORD = "password";
    private MemberDao memberDao;
    private Long memberId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        jdbcTemplate.update("insert into member (name, email, password, role) values (?, ?, ?, ?)",
                "테스터", EMAIL, PASSWORD, "USER");
        memberId = jdbcTemplate.queryForObject("select id from member where email = ?", Long.class, EMAIL);
    }

    @Test
    void 이메일과_비밀번호가_일치하면_회원을_반환한다() {
        // when
        Optional<Member> result = memberDao.findByEmailAndPassword(EMAIL, PASSWORD);

        // then
        assertThat(result).hasValueSatisfying(member -> {
            assertThat(member.getId()).isEqualTo(memberId);
            assertThat(member.getName()).isEqualTo("테스터");
            assertThat(member.getEmail()).isEqualTo(EMAIL);
            assertThat(member.getRole()).isEqualTo("USER");
        });
    }

    @ParameterizedTest
    @CsvSource({"login-dao@email.com, wrong", "unknown@email.com, password"})
    void 이메일이나_비밀번호가_일치하지_않으면_빈_결과를_반환한다(String email, String password) {
        // when
        Optional<Member> result = memberDao.findByEmailAndPassword(email, password);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 회원_ID로_회원을_조회한다() {
        // when
        Member member = memberDao.findById(memberId);

        // then
        assertThat(member.getId()).isEqualTo(memberId);
        assertThat(member.getName()).isEqualTo("테스터");
        assertThat(member.getEmail()).isEqualTo(EMAIL);
        assertThat(member.getRole()).isEqualTo("USER");
    }

    @Test
    void 존재하지_않는_회원_ID를_조회하면_저장소_예외를_던진다() {
        // given
        Long nonExistingId = -1L;

        // when & then
        assertThatThrownBy(() -> memberDao.findById(nonExistingId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
