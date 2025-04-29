package roomescape.login;

import jakarta.servlet.http.Cookie;
import java.util.Optional;
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

    public String getMemberName(String token) {
        Long memberId = jwtTokenService.getMemberId(token);
        MemberResponse member = memberService.findById(memberId);
        return member.getName();
    }

    public Optional<Long> getMemberId(String token) {
        Long memberId = jwtTokenService.getMemberId(token);
        return Optional.ofNullable(memberId);
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
