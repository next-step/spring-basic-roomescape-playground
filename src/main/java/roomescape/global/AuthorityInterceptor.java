package roomescape.global;

import static roomescape.auth.CookiesUtils.extractTokenFromCookie;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.JwtProvider;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Component
public class AuthorityInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public AuthorityInterceptor(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        String name = jwtProvider.getNameFromToken(token);
        MemberResponse member = memberService.findMemberByName(name);
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
