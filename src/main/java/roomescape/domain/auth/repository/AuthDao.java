package roomescape.domain.auth.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.auth.principal.LoginMember;

import java.util.Optional;

@Repository
public class AuthDao implements AuthRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuthDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<LoginMember> findByEmailAndPassword(String email, String password) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT id, name, email, role FROM member WHERE email = ? AND password = ?",
                    (resultSet, rowNum) -> new LoginMember(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("role")
                    ),
                    email, password
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
