package roomescape.reservation.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ReservationDao {
    private static final String RESERVATION_SELECT =
            "SELECT r.id AS reservation_id, r.name AS reservation_name, r.date AS reservation_date, " +
                    "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                    "ti.id AS time_id, ti.time_value AS time_value " +
                    "FROM reservation r " +
                    "JOIN theme t ON r.theme_id = t.id " +
                    "JOIN time ti ON r.time_id = ti.id ";

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getString("reservation_name"),
            rs.getString("reservation_date"),
            new Time(rs.getLong("time_id"), rs.getString("time_value")),
            new Theme(
                    rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("theme_description")
            )
    );

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(RESERVATION_SELECT, RESERVATION_ROW_MAPPER);
    }

    public boolean existsBySchedule(String date, Long themeId, Long timeId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND theme_id = ? AND time_id = ?)",
                Boolean.class,
                date,
                themeId,
                timeId
        ));
    }

    public Reservation save(String date, String memberName, Long themeId, Long timeId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation(date, name, theme_id, time_id) VALUES (?, ?, ?, ?)",
                    new String[]{"id"}
            );
            ps.setString(1, date);
            ps.setString(2, memberName);
            ps.setLong(3, themeId);
            ps.setLong(4, timeId);
            return ps;
        }, keyHolder);

        Time time = jdbcTemplate.queryForObject("SELECT * FROM time WHERE id = ?",
                (rs, rowNum) -> new Time(rs.getLong("id"), rs.getString("time_value")),
                timeId);

        Theme theme = jdbcTemplate.queryForObject("SELECT * FROM theme WHERE id = ?",
                (rs, rowNum) -> new Theme(rs.getLong("id"), rs.getString("name"), rs.getString("description")),
                themeId);

        return new Reservation(
                keyHolder.getKey().longValue(),
                memberName,
                date,
                time,
                theme
        );
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return jdbcTemplate.query(
                RESERVATION_SELECT + "WHERE r.date = ? AND r.theme_id = ?",
                RESERVATION_ROW_MAPPER,
                date,
                themeId
        );
    }
}
