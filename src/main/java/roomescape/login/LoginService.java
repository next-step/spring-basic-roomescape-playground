package roomescape.login;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Service
public class LoginService {

    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;

    public LoginService(MemberService memberService, JwtTokenService jwtTokenService) {
        this.memberService = memberService;
        this.jwtTokenService = jwtTokenService;
    }

    public String loginAndGetToken(LoginRequest loginRequest) {
        MemberResponse member = memberService.findByEmailAndPassword(loginRequest.getEmail(),
                                                                           loginRequest.getPassword());
        return jwtTokenService.getAccessToken(member.getId(), member.getName(), member.getRole());
    }

    public String getMemberName(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        Long memberId = jwtTokenService.getMemberId(token);
        MemberResponse member = memberService.findById(memberId);
        return member.getName();
    }

    public Long getMemberId(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        return jwtTokenService.getMemberId(token);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
