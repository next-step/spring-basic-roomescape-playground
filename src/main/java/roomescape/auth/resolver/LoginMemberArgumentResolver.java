package roomescape.auth.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.controller.LoginMember;
import roomescape.auth.service.AuthService;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.member.domain.Member;

import java.util.Arrays;

import static roomescape.auth.controller.AuthController.COOKIE_NAME;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public LoginMemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public LoginMember resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();

        Cookie cookie = findCookie(cookies);
        String accessToken = cookie.getValue();
        Member member = authService.getLoginMember(accessToken);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    //TODO: 책임 분리 필요?
    private Cookie findCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookieCandidate -> COOKIE_NAME.equals(cookieCandidate.getName()))
                .findAny()
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.COOKIE_NOT_FOUND.getMessage()));
    }
}
