package roomescape.member;

import static roomescape.member.Role.USER;

import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO member(name, email, password, role) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPassword());
            ps.setString(4, member.getRole());
            return ps;
        }, keyHolder);

        return new Member(keyHolder.getKey().longValue(), member.getName(), member.getEmail(), USER);
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        try {
            Member member = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, role FROM member WHERE email = ? AND password = ?",
                    (rs, rowNum) -> new Member(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            Role.valueOf(rs.getString("role"))
                    ),
                    email, password);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(Long id) {
        try {
            Member member = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, role FROM member WHERE id = ?",
                    (rs, rowNum) -> new Member(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            Role.valueOf(rs.getString("role"))
                    ), id
            );
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByName(String name) {
        try {
            Member member = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, role FROM member WHERE name = ?",
                    (rs, rowNum) -> new Member(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            Role.valueOf(rs.getString("role"))
                    ), name
            );
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
