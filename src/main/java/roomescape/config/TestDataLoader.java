package roomescape.config;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class TestDataLoader implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public TestDataLoader(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void run(String... args) {
        jdbcTemplate.execute("""
            INSERT INTO member (name, email, password, role)
            VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN'),
                   ('브라운', 'brown@email.com', 'password', 'USER');
            
            INSERT INTO theme (name, description)
            VALUES ('테마1', '테마1입니다.'),
                   ('테마2', '테마2입니다.'),
                   ('테마3', '테마3입니다.');
            
            INSERT INTO time (time_value)
            VALUES ('10:00'),
                   ('12:00'),
                   ('14:00'),
                   ('16:00'),
                   ('18:00'),
                   ('20:00');
            
            INSERT INTO reservation (member_id, name, date, time_id, theme_id)
            VALUES (1, '', '2024-03-01', 1, 1),
                   (1, '', '2024-03-01', 2, 2),
                   (1, '', '2024-03-01', 3, 3);
            
            INSERT INTO reservation (member_id, name, date, time_id, theme_id)
            VALUES (2, '브라운', '2024-03-01', 1, 2);
            """);
    }
}
