package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.AuthenticationException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthTokenService authTokenService;
    private final AuthCookieProvider authCookieProvider;

    public AuthUserArgumentResolver(AuthTokenService authTokenService, AuthCookieProvider authCookieProvider) {
        this.authTokenService = authTokenService;
        this.authCookieProvider = authCookieProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class)
                && (LoginMemberInfo.class.isAssignableFrom(parameter.getParameterType())
                || isOptionalLoginMemberInfo(parameter));
    }


    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        boolean optional = isOptionalLoginMemberInfo(parameter);
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            return handleAuthenticationFailure(optional);
        }

        try {
            String token = authCookieProvider.extractAccessToken(request);
            LoginMemberInfo loginMember = authTokenService.parseAccessToken(token);
            if (optional) {
                return Optional.of(loginMember);
            }
            return loginMember;
        } catch (AuthenticationException e) {
            return handleAuthenticationFailure(optional);
        }
    }

    private Object handleAuthenticationFailure(boolean optional) {
        if (optional) {
            return Optional.empty();
        }
        throw new AuthenticationException();
    }

    private boolean isOptionalLoginMemberInfo(MethodParameter parameter) {
        if (!Optional.class.isAssignableFrom(parameter.getParameterType())) {
            return false;
        }
        Type genericParameterType = parameter.getGenericParameterType();
        if (!(genericParameterType instanceof ParameterizedType parameterizedType)) {
            return false;
        }
        Type actualType = parameterizedType.getActualTypeArguments()[0];
        return LoginMemberInfo.class.getName().equals(actualType.getTypeName());
    }
}
