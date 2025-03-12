package roomescape.auth;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final String TOKEN_PREFIX = "token";

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Long getUserIdFromToken(String token) {
        return jwtTokenProvider.getIdFromToken(token);
    }

    public String getTokenFromCookies(Cookie[] cookies) {
        return Optional.ofNullable(cookies).flatMap(c -> Arrays.stream(c)
                        .filter(cookie -> TOKEN_PREFIX.equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst())
                .orElse(null);
    }
}
