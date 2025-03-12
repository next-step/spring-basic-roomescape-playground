package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.CookieExtractor;
import roomescape.auth.AuthService;
import roomescape.member.LoginMember;
import roomescape.member.Member;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final CookieExtractor cookieExtractor;

    public LoginMemberArgumentResolver(AuthService authService, CookieExtractor cookieExtractor) {
        this.authService = authService;
        this.cookieExtractor = cookieExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie cookie = cookieExtractor.extractToken(request);

        if (cookie == null) {
            return null;
        }

        Member member = authService.findMemberByToken(cookie.getValue());
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

}
