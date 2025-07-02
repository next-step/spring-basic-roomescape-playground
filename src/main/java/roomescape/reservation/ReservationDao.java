package roomescape.reservation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.sql.PreparedStatement;
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
                        rs.getString("reservation_date"),
                        new Time(
                                rs.getLong("time_id"),
                                rs.getString("time_value")
                        ),
                        new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_description")
                        )));
    }

    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation(date, name, time_id, theme_id) VALUES (?, ?, ?, ?)", new String[]{"id"});
            ps.setString(1, reservation.getDate());
            ps.setString(2, reservation.getName());
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public List<Reservation> findReservationsByDateAndTheme(String date, Long themeId) {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id" +
                        "WHERE r.date = ? AND r.theme_id = ?",
                new Object[]{date, themeId},
                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("reservation_name"),
                        rs.getString("reservation_date"),
                        new Time(
                                rs.getLong("time_id"),
                                rs.getString("time_value")
                        ),
                        new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_description")
                        )));
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id " +
                        "WHERE r.date = ? AND r.theme_id = ?",
                new Object[]{date, themeId},
                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("reservation_name"),
                        rs.getString("reservation_date"),
                        new Time(
                                rs.getLong("time_id"),
                                rs.getString("time_value")
                        ),
                        new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_description")
                        )));
    }
}
