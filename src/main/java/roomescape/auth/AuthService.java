package roomescape.auth;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String createToken(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword())
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다."));
        return jwtTokenProvider.createToken(LoginMember.from(member));
    }

    public Optional<LoginMember> extractMember(Cookie[] cookies) {
        return extractTokenFromCookie(cookies)
                .map(jwtTokenProvider::extractLoginMember);
    }

    private Optional<String> extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> AuthCookie.TOKEN_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(token -> !token.isBlank())
                .findFirst();
    }
}
