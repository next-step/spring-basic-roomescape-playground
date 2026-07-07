package roomescape.reservation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id",
                this::mapReservation);
    }

    public Reservation save(ReservationRequest reservationRequest, String name) {
        Number id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                "date", reservationRequest.getDate(),
                "name", name,
                "theme_id", reservationRequest.getTheme(),
                "time_id", reservationRequest.getTime()
        ));

        Time time = jdbcTemplate.queryForObject("SELECT * FROM time WHERE id = ?",
                (rs, rowNum) -> new Time(rs.getLong("id"), rs.getString("time_value")),
                reservationRequest.getTime());

        Theme theme = jdbcTemplate.queryForObject("SELECT * FROM theme WHERE id = ?",
                (rs, rowNum) -> new Theme(rs.getLong("id"), rs.getString("name"), rs.getString("description")),
                reservationRequest.getTheme());

        return new Reservation(
                id.longValue(),
                name,
                reservationRequest.getDate(),
                time,
                theme
        );
    }

    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id) > 0;
    }

    public List<Reservation> findByMemberName(String name) {
        return namedParameterJdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id " +
                        "WHERE r.name = :name",
                Map.of("name", name),
                this::mapReservation);
    }

    public List<Reservation> findReservationsByDateAndTheme(String date, Long themeId) {
        return namedParameterJdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id " +
                        "WHERE r.date = :date AND r.theme_id = :themeId",
                Map.of("date", date, "themeId", themeId),
                this::mapReservation);
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return namedParameterJdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id " +
                        "WHERE r.date = :date AND r.theme_id = :themeId",
                Map.of("date", date, "themeId", themeId),
                this::mapReservation);
    }

    private Reservation mapReservation(ResultSet rs, int rowNum) throws SQLException {
        return new Reservation(
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
                ));
    }
}
