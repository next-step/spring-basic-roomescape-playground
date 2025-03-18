package roomescape.auth.client.cookie;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import roomescape.global.exception.RoomescapeUnauthorizedException;

@Component
public class CookieResolver {

    public String getToken(Cookie[] cookies) {
        String accessToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(CookieProperties.TOKEN))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RoomescapeUnauthorizedException("로그인 되어있지 않습니다."));

        return accessToken;
    }
}
