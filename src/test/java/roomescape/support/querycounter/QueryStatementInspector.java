package roomescape.support.querycounter;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.ArrayList;
import java.util.List;

public class QueryStatementInspector implements StatementInspector {

    private static final ThreadLocal<List<String>> QUERIES = new ThreadLocal<>();

    public void start() {
        QUERIES.set(new ArrayList<>());
    }

    public void clear() {
        QUERIES.remove();
    }

    public List<String> getQueriesResult() {
        List<String> queries = QUERIES.get();
        return queries == null ? List.of() : queries;
    }

    @Override
    public String inspect(String sql) {
        List<String> queries = QUERIES.get();
        if (queries != null) {
            queries.add(sql);
        }
        return sql;
    }
}
