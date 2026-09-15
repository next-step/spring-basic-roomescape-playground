package roomescape.member;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    private JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement("INSERT INTO member(name, email, password, role) VALUES (?, ?, ?, ?)", new String[]{"id"});
            preparedStatement.setString(1, member.getName());
            preparedStatement.setString(2, member.getEmail());
            preparedStatement.setString(3, member.getPassword());
            preparedStatement.setString(4, member.getRole().name());
            return preparedStatement;
        }, keyHolder);

        return new Member(keyHolder.getKey().longValue(), member.getName(), member.getEmail(), member.getRole());
    }

    public Member findByEmailAndPassword(String email, String password) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE email = ? AND password = ?",
                (resultSet, rowNum) -> new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        Role.valueOf(resultSet.getString("role"))
                ),
                email, password
        );
    }

    public Member findByName(String name) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE name = ?",
                (resultSet, rowNum) -> new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        Role.valueOf(resultSet.getString("role"))
                ),
                name
        );
    }

    public Member findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE id = ?",
                (resultSet, rowNum) -> new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        Role.valueOf(resultSet.getString("role"))
                ),
                id
        );
    }
}
