package roomescape.member;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.enums.Role;

@Repository
public class MemberDao {

    private final RowMapper<Member> mapper = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            Role.valueOf(rs.getString("role"))
    );

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("INSERT INTO member(name, email, password, role) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPassword());
            ps.setString(4, member.getRole().name());
            return ps;
        }, keyHolder);

        return new Member(keyHolder.getKey().longValue(), member.getName(), member.getEmail(), member.getRole());
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        try {
            String sql = "SELECT id, name, email, role FROM member WHERE email = ? AND password = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapper, email, password));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByName(String name) {
        try {
            String sql = "SELECT id, name, email, role FROM member WHERE name = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapper, name));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(Long id) {
        try {
            String query = "SELECT id, name, email, role FROM member WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, mapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
