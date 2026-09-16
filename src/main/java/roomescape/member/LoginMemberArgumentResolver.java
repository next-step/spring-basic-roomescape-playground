package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver
        implements HandlerMethodArgumentResolver {

    private final LoginService loginService;

    public LoginMemberArgumentResolver(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request =
                webRequest.getNativeRequest(HttpServletRequest.class);

        String token = extractTokenFromCookie(
                request == null ? null : request.getCookies()
        );

        return loginService.findLoginMember(token);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return "";
    }
}