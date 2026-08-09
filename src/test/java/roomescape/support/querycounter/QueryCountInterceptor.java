package roomescape.support.querycounter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public class QueryCountInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(QueryCountInterceptor.class);

    private final QueryStatementInspector queryStatementInspector;

    public QueryCountInterceptor(QueryStatementInspector queryStatementInspector) {
        this.queryStatementInspector = queryStatementInspector;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        queryStatementInspector.start();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        List<String> queries = queryStatementInspector.getQueriesResult();
        String report = generateQueriesReport(request, queries);

        log.info(report);
        queryStatementInspector.clear();
    }

    private String generateQueriesReport(HttpServletRequest request, List<String> queries) {
        return "[QueryCount] " + request.getMethod() + " " + request.getRequestURI() + System.lineSeparator()
                + String.join(System.lineSeparator(), queries)
                + System.lineSeparator()
                + NPlusOneWarnings.getWarningMessage(queries);
    }
}