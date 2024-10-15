package roomescape.config;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Profile("production")
@Component
public class DataLoader implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DataLoader(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void run(String... args) {
        jdbcTemplate.execute("""
            INSERT INTO member (name, email, password, role)
            VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN'),
                   ('브라운', 'brown@email.com', 'password', 'USER');
            """);
    }
}
