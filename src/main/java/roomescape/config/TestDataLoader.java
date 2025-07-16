package roomescape.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class TestDataLoader implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public TestDataLoader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Member (ID: 1=어드민, 2=브라운, 3=클로이)
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN')");
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES ('브라운', 'brown@email.com', 'password', 'USER')");
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES ('클로이', 'chloe@email.com', 'password', 'USER')");

        // Theme
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('공포', '매우 무서운 테마', 'https://i.imgur.com/1.jpg')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('코믹', '매우 웃긴 테마', 'https://i.imgur.com/2.jpg')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('어드벤처', '신나는 모험 테마', 'https://i.imgur.com/3.jpg')");

        // Time
        jdbcTemplate.update("INSERT INTO time (time) VALUES ('10:00')");
        jdbcTemplate.update("INSERT INTO time (time) VALUES ('13:00')");
        jdbcTemplate.update("INSERT INTO time (time) VALUES ('15:00')");

        // Reservation
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, theme_id, time_id) VALUES ('2024-03-01', 1, 1, 1)");
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, theme_id, time_id) VALUES ('2024-03-01', 1, 1, 1)");
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, theme_id, time_id) VALUES ('2024-03-01', 1, 1, 1)");
    }
}
