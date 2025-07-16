package roomescape.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class DataLoader implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DataLoader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN')");

        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('공포', '매우 무서운 테마', 'https://i.imgur.com/1.jpg')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('코믹', '매우 웃긴 테마', 'https://i.imgur.com/2.jpg')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('어드벤처', '신나는 모험 테마', 'https://i.imgur.com/3.jpg')");

        jdbcTemplate.update("INSERT INTO time (time) VALUES ('10:00')");
        jdbcTemplate.update("INSERT INTO time (time) VALUES ('13:00')");
        jdbcTemplate.update("INSERT INTO time (time) VALUES ('15:00')");
    }
}
