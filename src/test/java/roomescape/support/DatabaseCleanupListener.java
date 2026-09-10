package roomescape.support;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class DatabaseCleanupListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        JdbcTemplate jdbcTemplate = testContext.getApplicationContext()
                .getBean(JdbcTemplate.class);

        jdbcTemplate.update("DELETE FROM member");
        jdbcTemplate.update("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?), (?, ?, ?, ?)",
                "어드민", "admin@email.com", "password", "ADMIN",
                "브라운", "brown@email.com", "password", "USER"
        );
    }
}
