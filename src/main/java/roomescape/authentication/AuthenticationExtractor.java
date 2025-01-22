package roomescape.authentication;

import jakarta.servlet.http.Cookie;

public interface AuthenticationExtractor {

    MemberAuthInfo extractMemberAuthInfoFromToken(String token);

    AuthenticationResponse extractTokenFromCookie(Cookie[] cookies);
}
