package roomescape;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

public class DataBaseCleaner implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        ApplicationContext context = SpringExtension.getApplicationContext(extensionContext);
        cleanup(context);
    }

    private void cleanup(ApplicationContext context) {
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        for (String tableName : findTableNames(jdbcTemplate)) {
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName + " RESTART IDENTITY");
        }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private List<String> findTableNames(JdbcTemplate jdbcTemplate) {
        String tableNameSelectQuery = """
                SELECT TABLE_NAME
                FROM INFORMATION_SCHEMA.TABLES
                WHERE TABLE_SCHEMA = 'PUBLIC'
                """;
        return jdbcTemplate.queryForList(tableNameSelectQuery, String.class);
    }
}
