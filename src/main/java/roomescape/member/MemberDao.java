package roomescape.member;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {
    private JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> rowMapper = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            Role.valueOf(rs.getString("role"))
    );

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

        return new Member(keyHolder.getKey().longValue(), member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }

    public Member findByEmailAndPassword(String email, String password) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE email = ? AND password = ?",
                rowMapper,
                email, password
        );
    }

    public Member findByName(String name) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE name = ?",
                rowMapper,
                name
        );
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, name, email, role FROM member WHERE id = ?";

        List<Member> results = jdbcTemplate.query(sql, rowMapper, id);

        return results.stream().findFirst();
    }
}
