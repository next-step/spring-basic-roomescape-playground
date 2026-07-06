package roomescape.member.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

import java.util.Optional;

@Repository
public class MemberDao {
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
            ps.setString(4, member.getRole().name());
            return ps;
        }, keyHolder);

        return new Member(keyHolder.getKey().longValue(), member.getName(), member.getEmail(), Role.ADMIN);
    }

    public Optional<Member> findById(Long id) {
        return jdbcTemplate.query(
                "SELECT * FROM member WHERE id = ?",
                rowMapper(),
                id
        ).stream().findFirst();
    }

    public Member findByEmailAndPassword(String email, String password) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE email = ? AND password = ?",
                rowMapper(),
                email, password
        );
    }

    public Optional<Member> findByName(String name) {
        return jdbcTemplate.query(
                "SELECT id, name, email, role FROM member WHERE name = ?",
                rowMapper(),
                name
        ).stream().findFirst();
    }

    private RowMapper<Member> rowMapper() {
        return (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                Role.valueOf(resultSet.getString("role"))
        );
    }
}
