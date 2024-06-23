package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationProvider authorizationProvider;

    public AuthMemberArgumentResolver(AuthorizationProvider authorizationProvider) {
        this.authorizationProvider = authorizationProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authentication.class)
                && parameter.getParameterType().equals(MemberAuthContext.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String token = parseTokenFromNativeRequest((ServletWebRequest) webRequest);
        MemberCredential memberCredential = new MemberCredential(token);
        return authorizationProvider.parseCredential(memberCredential);
    }

    private String parseTokenFromNativeRequest(ServletWebRequest webRequest) {
        HttpServletRequest httpServletRequest = webRequest.getRequest();
        return Arrays.stream(httpServletRequest.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
    }
}
