package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

public class TokenExtractor {

    public static String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RoomEscapeException(ErrorCode.INVALID_TOKEN);
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new RoomEscapeException(ErrorCode.INVALID_TOKEN);
    }
}
