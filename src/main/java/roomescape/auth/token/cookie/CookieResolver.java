package roomescape.auth.token.cookie;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import roomescape.auth.token.jwt.JwtProperties;
import roomescape.global.exception.RoomescapeUnauthorizedException;

@Component
public class CookieResolver {

    public String getToken(Cookie[] cookies) {
        String accessToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(JwtProperties.TOKEN))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RoomescapeUnauthorizedException("로그인 되어있지 않습니다."));

        return accessToken;
    }
}
