package roomescape.auth;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(tokenRequest.getPassword(), member.getPassword())) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getName(), member.getEmail(), member.getRole());
    }

    public Cookie createTokenCookie(TokenRequest tokenRequest) {
        return AuthCookie.createTokenCookie(createToken(tokenRequest));
    }

    public Cookie createExpiredTokenCookie() {
        return AuthCookie.createExpiredTokenCookie();
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
