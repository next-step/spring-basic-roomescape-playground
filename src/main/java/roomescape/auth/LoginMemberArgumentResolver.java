package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor authorizationExtractor;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider,
                                       AuthorizationExtractor authorizationExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = authorizationExtractor.extract((HttpServletRequest) webRequest.getNativeRequest());
        Map<String, Object> claims = jwtTokenProvider.getClaims(token);
        return LoginMember.fromClaims(claims);
    }
}
