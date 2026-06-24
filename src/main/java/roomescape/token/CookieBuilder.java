package roomescape.token;

import org.springframework.http.ResponseCookie;

public class CookieBuilder {
    public static ResponseCookie createTokenCookie(String token){
        return ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .maxAge(3600)
                .build();
    }

    public static ResponseCookie createEmptyCookie() {
        return ResponseCookie.from("token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
    }
    
}
