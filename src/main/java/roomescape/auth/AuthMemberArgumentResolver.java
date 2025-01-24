package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

@Component
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String TOKEN_VALUE = "token";
    private final TokenGenerator tokenGenerator;

    public AuthMemberArgumentResolver(@Autowired TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isAuthenticationAnnotation = parameter.hasParameterAnnotation(Authentication.class);
        boolean isAuthCredential = parameter.getParameterType().equals(AuthCredential.class);

        return isAuthenticationAnnotation && isAuthCredential;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = extractTokenFromRequest((ServletWebRequest) webRequest);
        AuthCredential authCredential = tokenGenerator.parseAccessToken(token);
        return authCredential;
    }

    private String extractTokenFromRequest(ServletWebRequest webRequest) {
        Cookie[] cookies = webRequest.getRequest().getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_VALUE))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
    }
}
