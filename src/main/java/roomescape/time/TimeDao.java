package roomescape.time;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeDao {
    private final JdbcTemplate jdbcTemplate;

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Time> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM time WHERE deleted = false",
                (rs, rowNum) -> new Time(
                        rs.getLong("id"),
                        rs.getString("time_value")));
    }

    public Time save(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO time(time_value) VALUES (?)", new String[]{"id"});
            ps.setString(1, time.getValue());
            return ps;
        }, keyHolder);

        return new Time(keyHolder.getKey().longValue(), time.getValue());
    }

    public Optional<Time> findById(Long id) {
        return jdbcTemplate.query(
                "SELECT id, time_value FROM time WHERE id = ? AND deleted = false",
                (resultSet, rowNumber) -> new Time(
                        resultSet.getLong("id"),
                        resultSet.getString("time_value")
                ),
                id
        ).stream().findFirst();
    }

    public Optional<Time> findByValue(String value) {
        return jdbcTemplate.query(
                "SELECT id, time_value FROM time WHERE time_value = ?",
                (resultSet, rowNumber) -> new Time(
                        resultSet.getLong("id"),
                        resultSet.getString("time_value")
                ),
                value
        ).stream().findFirst();
    }

    public int restoreById(Long id) {
        return jdbcTemplate.update(
                "UPDATE time SET deleted = false WHERE id = ? AND deleted = true",
                id
        );
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("UPDATE time SET deleted = true WHERE id = ? AND deleted = false", id);
    }
}
