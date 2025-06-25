package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public AuthService(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    public TokenResponse login(LoginRequest loginRequest) {
        Member member = memberService.authenticate(loginRequest);
        String token = jwtProvider.createToken(member);
        return new TokenResponse(token);
    }

    public MemberResponse findMemberByToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        Long memberId = jwtProvider.extractMemberId(token);
        return memberService.findById(memberId);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException("쿠키가 존재하지 않습니다");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException("토큰 쿠키가 존재하지 않습니다.");
    }
}
