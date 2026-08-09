package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.CookieManager;
import roomescape.JwtProvider;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final CookieManager cookieManager;

    public LoginInterceptor(JwtProvider jwtProvider, MemberRepository memberRepository, CookieManager cookieManager) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
        this.cookieManager = cookieManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = getCookies(request);

        String token = cookieManager.extractToken(cookies,"accessToken");
        Long memberId = jwtProvider.getMemberId(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow();
        if (!member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }

    private Cookie[] getCookies(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            throw new IllegalStateException("cookie is not exist");
        }
        return cookies;
    }
}
