package roomescape.cookie;

import jakarta.servlet.http.Cookie;

public class CookieService {
    public static String extractTokenFromCookie(Cookie[] cookies){
        for(Cookie cookie: cookies){
            if(cookie.getName().equals("token")){
                return cookie.getValue();
            }
        }
        return "";
    }
}
