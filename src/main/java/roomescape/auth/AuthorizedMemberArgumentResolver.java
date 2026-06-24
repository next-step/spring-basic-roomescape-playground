package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthorizedMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthTokenProvider authTokenProvider;

    public AuthorizedMemberArgumentResolver(AuthTokenProvider authTokenProvider) {
        this.authTokenProvider = authTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == AuthorizedMember.class;
    }

    @Override
    public AuthorizedMember resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new ErrorResponseException(HttpStatus.UNAUTHORIZED);
        }

        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("token")) {
                continue;
            }

            AuthToken token = new AuthToken(cookie.getValue());
            return authTokenProvider.parseSessionToken(token);
        }

        throw new ErrorResponseException(HttpStatus.UNAUTHORIZED);
    }
}
