package roomescape.domain.reservation.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id",

                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("reservation_name"),
                        rs.getObject("reservation_date", LocalDate.class),
                        new Time(
                                rs.getLong("time_id"),
                                rs.getObject("time_value", LocalTime.class)
                        ),
                        new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_description")
                        )));
    }

    public Reservation save(String name, LocalDate date, Long themeId, Long timeId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation(date, name, theme_id, time_id) VALUES (?, ?, ?, ?)", new String[]{"id"});
            ps.setString(1, date.toString());
            ps.setString(2, name);
            ps.setLong(3, themeId);
            ps.setLong(4, timeId);
            return ps;
        }, keyHolder);

        Time time = jdbcTemplate.queryForObject("SELECT * FROM time WHERE id = ?",
                (rs, rowNum) -> new Time(rs.getLong("id"), rs.getObject("time_value", LocalTime.class)),
                timeId);

        Theme theme = jdbcTemplate.queryForObject("SELECT * FROM theme WHERE id = ?",
                (rs, rowNum) -> new Theme(rs.getLong("id"), rs.getString("name"), rs.getString("description")),
                themeId);

        return new Reservation(
                keyHolder.getKey().longValue(),
                name,
                date,
                time,
                theme
        );
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public List<Reservation> findReservationsByDateAndTheme(LocalDate date, Long themeId) {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id" +
                        "WHERE r.date = ? AND r.theme_id = ?",
                new Object[]{date.toString(), themeId},
                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("reservation_name"),
                        rs.getObject("reservation_date", LocalDate.class),
                        new Time(
                                rs.getLong("time_id"),
                                rs.getObject("time_value", LocalTime.class)
                        ),
                        new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_description")
                        )));
    }

    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id " +
                        "WHERE r.date = ? AND r.theme_id = ?",
                new Object[]{date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), themeId},
                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("reservation_name"),
                        rs.getObject("reservation_date",  LocalDate.class),
                        new Time(
                                rs.getLong("time_id"),
                                rs.getObject("time_value", LocalTime.class)
                        ),
                        new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_description")
                        )));
    }
}
