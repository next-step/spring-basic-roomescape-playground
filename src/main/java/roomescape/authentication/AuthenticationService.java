package roomescape.authentication;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.Member;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationExtractor authenticationExtractor;

    public AuthenticationResponse createToken(Member member) {
        return authenticationProvider.createAuthenticationMethod(member);
    }

    public AuthenticationResponse extractToken(Cookie [] cookies){
        return authenticationExtractor.extractTokenFromCookie(cookies);
    }

    public MemberAuthInfo extractMemberInfo(String token) {
        return authenticationExtractor.extractMemberAuthInfoFromToken(token);
    }

    public MemberAuthInfo getMemberAuthInfoFromCookies(Cookie[] cookies) {
        AuthenticationResponse authenticationResponse = extractToken(cookies);
        return extractMemberInfo(authenticationResponse.accessToken());
    }
}
