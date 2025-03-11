package roomescape.member.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

import java.util.Objects;
import java.util.Optional;

@Repository
public class MemberDao {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("role")
            );

    private final JdbcTemplate jdbcTemplate;

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

        return new Member(Objects.requireNonNull(keyHolder.getKey()).longValue(), member.getName(), member.getEmail(), "USER");
    }

    public Optional<Member> findById(long memberId) {
        try {
            String selectById = """
                    SELECT id,
                           name,
                           email,
                           role
                    FROM member
                    WHERE id = ?
                    """;
            Member member = jdbcTemplate.queryForObject(selectById, MEMBER_ROW_MAPPER, memberId);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        try {
            String selectByEmailAndPassword = """
                    SELECT id,
                           name,
                           email,
                           role
                    FROM member
                    WHERE email = ? AND password = ?
                    """;
            Member member = jdbcTemplate.queryForObject(selectByEmailAndPassword, MEMBER_ROW_MAPPER, email, password);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }
}
