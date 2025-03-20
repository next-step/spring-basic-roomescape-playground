package roomescape.time.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.time.domain.Time;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeDao {

    private static final RowMapper<Time> TIME_ROW_MAPPER = (rs, rowNum) ->
            new Time(
                    rs.getLong("id"),
                    rs.getTime("time_value").toLocalTime()
            );

    private final JdbcTemplate jdbcTemplate;

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Time> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM time WHERE deleted = false",
                (rs, rowNum) -> new Time(
                        rs.getLong("id"),
                        rs.getTime("time_value").toLocalTime()));
    }

    public Time save(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO time(time_value) VALUES (?)", new String[]{"id"});
            ps.setTime(1, java.sql.Time.valueOf(time.getValue()));
            return ps;
        }, keyHolder);

        return new Time(keyHolder.getKey().longValue(), time.getValue());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("UPDATE time SET deleted = true WHERE id = ?", id);
    }

    public Optional<Time> findById(Long timeId) {
        try {
            String selectById = "SELECT id, time_value FROM time WHERE id = ?";
            Time time = jdbcTemplate.queryForObject(selectById, TIME_ROW_MAPPER, timeId);
            return Optional.ofNullable(time);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }
}
