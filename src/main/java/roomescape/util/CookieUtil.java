package roomescape.util;

import jakarta.servlet.http.Cookie;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;
import java.util.Optional;

public class CookieUtil {

    public static Optional<String> extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    public static Optional<String> extractTokenFromHeader(NativeWebRequest webRequest) {
        String cookieHeader = webRequest.getHeader("Cookie");
        if (cookieHeader == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookieHeader.split(";"))
                .map(String::trim)
                .filter(cookie -> cookie.startsWith("token="))
                .map(cookie -> cookie.substring("token=".length()))
                .findFirst();
    }
}
