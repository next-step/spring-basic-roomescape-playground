package roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberDao {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = ((rs, rowNum) -> {
        return new Member(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("role")
        );
    });

    private JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("INSERT INTO member(name, email, password, role) VALUES (?, ?, ?, ?)", new String[]{"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPassword());
            ps.setString(4, member.getRole());
            return ps;
        }, keyHolder);

        return new Member(keyHolder.getKey().longValue(), member.getName(), member.getEmail(), "USER");
    }

    public Optional<Member> findByEmailAndPassword(final String email, final String password) {
        try {
            final String selectByEmailAndPassword = """
                    SELECT id,
                           name,
                           email,
                           role
                    FROM member
                    WHERE email = ? AND password = ?
                    """;
            final Member member = jdbcTemplate.queryForObject(selectByEmailAndPassword, MEMBER_ROW_MAPPER, email, password);
            return Optional.ofNullable(member);
        } catch (final EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }

    public Member findByName(String name) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE name = ?",
                (rs, rowNum) -> new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role")
                ),
                name
        );
    }
}
