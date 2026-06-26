package roomescape.token;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenUtils {
    String extractToken(HttpServletRequest request);

    void appendToken(String token, HttpServletResponse response);

    void removeToken(HttpServletResponse response);
}
