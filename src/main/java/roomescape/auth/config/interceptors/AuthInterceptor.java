package roomescape.auth.config.interceptors;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.Public;
import roomescape.auth.config.AllowedRole;
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
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String token = null;
        MemberResponse member = null;

        AllowedRole allowedRole = handlerMethod.getMethodAnnotation(AllowedRole.class);
        if (allowedRole == null) {
            allowedRole = handlerMethod.getBeanType().getAnnotation(AllowedRole.class);
        }

        // 인증 필요없는 메소드
        if (handlerMethod.hasMethodAnnotation(Public.class) ||
                handlerMethod.getBeanType().isAnnotationPresent(Public.class)) {
            return true;
        }

        if (request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if (token == null) {
            if (allowedRole != null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "먼저 로그인을 해주세요.");
                return false;
            }
            return true;
        }

        try {
            String email = tokenProvider.getPayload(token);
            member = memberService.findByEmail(email);
            request.setAttribute("member", member);
            request.setAttribute("email", email);
        } catch (Exception e) {
            if (allowedRole != null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰이에요!");
                return false;
            }
        }


        if (allowedRole != null) {
            if (member == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "먼저 로그인을 해주세요!");
                return false;
            }

            if (!member.getRole().isAuthorized(allowedRole.value())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "앗! 접근 권한이 없어요!");
                return false;
            }
        }

        return true;
    }
}