package roomescape.auth.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.auth.domain.RefreshToken;

import java.util.Optional;

@Repository
public class RefreshTokenDao {

    private JdbcTemplate jdbcTemplate;

    public RefreshTokenDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(RefreshToken refreshToken) {
        jdbcTemplate.update(
                "INSERT INTO refresh_token(member_id, token) VALUES (?, ?)",
                refreshToken.getMemberId(),
                refreshToken.getToken()
        );
    }

    public Optional<RefreshToken> findByToken(String token) {
        return jdbcTemplate.query(
                "SELECT id, member_id, token FROM refresh_token WHERE token = ?",
                rowMapper(),
                token
        ).stream().findFirst();
    }

    public Optional<RefreshToken> findByMemberId(Long memberId) {
        return jdbcTemplate.query(
                "SELECT id, member_id, token FROM refresh_token WHERE member_id = ?",
                rowMapper(),
                memberId
        ).stream().findFirst();
    }

    public void deleteByMemberId(Long memberId) {
        jdbcTemplate.update("DELETE FROM refresh_token WHERE member_id = ?", memberId);
    }

    private RowMapper<RefreshToken> rowMapper() {
        return (resultSet, rowNum) -> new RefreshToken(
                resultSet.getLong("id"),
                resultSet.getLong("member_id"),
                resultSet.getString("token")
        );
    }
}
