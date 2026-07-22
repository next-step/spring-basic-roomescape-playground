package roomescape;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clear() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        jdbcTemplate.execute("TRUNCATE TABLE refresh_token RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE waiting RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE reservation RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE time RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE theme RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE member RESTART IDENTITY");

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        insertInitialData();
    }

    private void insertInitialData() {
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) " +
                        "VALUES " +
                        "('어드민', 'admin@email.com', 'password', 'ADMIN'), " +
                        "('브라운', 'brown@email.com', 'password', 'USER')"
        );

        jdbcTemplate.update(
                "INSERT INTO theme (name, description) " +
                        "VALUES " +
                        "('테마1', '테마1입니다.'), " +
                        "('테마2', '테마2입니다.'), " +
                        "('테마3', '테마3입니다.')"
        );

        jdbcTemplate.update(
                "INSERT INTO `time` (time_value) " +
                        "VALUES " +
                        "('10:00'), " +
                        "('12:00'), " +
                        "('14:00'), " +
                        "('16:00'), " +
                        "('18:00'), " +
                        "('20:00')"
        );

        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) " +
                        "VALUES " +
                        "(1, '2024-03-01', 1, 1), " +
                        "(1, '2024-03-01', 2, 2), " +
                        "(1, '2024-03-01', 3, 3), " +
                        "(2, '2024-03-01', 1, 2)"
        );
    }
}
