package roomescape.domain.time.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.entity.Time;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;

@Repository
public class TimeDao implements TimeRepository {
    private final JdbcTemplate jdbcTemplate;

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Time> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM time WHERE deleted = false",
                (rs, rowNum) -> new Time(
                        rs.getLong("id"),
                        rs.getObject("time_value", LocalTime.class)));
    }

    @Override
    public Time save(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO time(time_value) VALUES (?)", new String[]{"id"});
            ps.setObject(1, time.getValue());
            return ps;
        }, keyHolder);

        return new Time(keyHolder.getKey().longValue(), time.getValue());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("UPDATE time SET deleted = true WHERE id = ?", id);
    }
}
