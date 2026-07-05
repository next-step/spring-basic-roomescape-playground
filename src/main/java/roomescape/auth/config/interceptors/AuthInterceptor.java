package roomescape.auth.config.interceptors;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.config.utils.TokenProvider;
import roomescape.member.DTO.MemberResponse;
import roomescape.member.MemberService;

import java.util.Arrays;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    public AuthInterceptor(TokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;

        if (request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if (token == null) {
            return true;
        }

        try {
            String email = tokenProvider.getPayload(token);
            MemberResponse member = memberService.findByEmail(email);
            request.setAttribute("member", member);
            request.setAttribute("email", email);
        } catch (Exception e) {
            System.err.println("인증 처리 중 오류가 발생했어요. : " + e.getMessage());
        }

        return true;
    }
}