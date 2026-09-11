package roomescape.time.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.time.domain.Time;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TimeDao {
    private static final String TIME_SELECT = "SELECT id, time_value FROM time ";
    private static final RowMapper<Time> TIME_ROW_MAPPER = (rs, rowNum) -> new Time(
            rs.getLong("id"),
            rs.getString("time_value")
    );

    private final JdbcTemplate jdbcTemplate;

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Time> findAll() {
        return jdbcTemplate.query(TIME_SELECT + "WHERE deleted = false", TIME_ROW_MAPPER);
    }

    public Time findById(Long id) {
        return jdbcTemplate.queryForObject(
                TIME_SELECT + "WHERE id = ? AND deleted = false",
                TIME_ROW_MAPPER,
                id
        );
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

    public void deleteById(Long id) {
        jdbcTemplate.update("UPDATE time SET deleted = true WHERE id = ?", id);
    }
}
