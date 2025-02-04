package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthorizationExtractor {
    String extract(HttpServletRequest request);
}
