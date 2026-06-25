package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.ApiException;

@SuppressWarnings("unchecked")
@Component
public class AuthorizedMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthTokenProvider authTokenProvider;

    public AuthorizedMemberArgumentResolver(AuthTokenProvider authTokenProvider) {
        this.authTokenProvider = authTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (AuthorizedMember.class.isAssignableFrom(parameter.getParameterType())) {
            return true;
        }

        if (parameter.getParameterType() == Optional.class) {
            Class<?> innerType = extractSingleGenericType(parameter);
            return innerType != null && AuthorizedMember.class.isAssignableFrom(innerType);
        }

        return false;
    }

    private Class<?> extractSingleGenericType(MethodParameter parameter) {
        ResolvableType type = ResolvableType.forMethodParameter(parameter);
        return type.getGeneric(0).resolve();
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Class<?> parameterType = parameter.getParameterType();
        if (AuthorizedMember.class.isAssignableFrom(parameterType)) {
            return resolveAuthorizedMember((Class<? extends AuthorizedMember>) parameterType, request);
        }

        if (parameterType == Optional.class) {
            var innerType = (Class<? extends AuthorizedMember>) extractSingleGenericType(parameter);
            return resolveAuthorizedMemberOptional(innerType, request);
        }

        throw new AssertionError("unexhaustive code for type: " + parameter);
    }

    private AuthorizedMember resolveAuthorizedMember(
            Class<? extends AuthorizedMember> type,
            HttpServletRequest request
    ) {
        Optional<AuthorizedMember> member = resolveAuthorizedMemberOptional(type, request);
        return member.orElseThrow(() -> ApiException.status(HttpStatus.UNAUTHORIZED));
    }

    private Optional<AuthorizedMember> resolveAuthorizedMemberOptional(
            Class<? extends AuthorizedMember> type,
            HttpServletRequest request
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("token")) {
                continue;
            }

            AuthToken token = new AuthToken(cookie.getValue());
            AuthorizedMember member = authTokenProvider.parseSessionToken(token);

            if (type != AuthorizedMember.class) {
                Class<?>[] parameterTypes = AuthorizedMember.class.getDeclaredConstructors()[0].getParameterTypes();
                try {
                    var constructor = type.getDeclaredConstructor(parameterTypes);
                    //noinspection JavaReflectionInvocation: Intellij가 parameterTypes이 유일한 argument의 타입인 것으로 착각
                    member = constructor.newInstance(member.name(), member.email(), member.role());
                } catch (ReflectiveOperationException e) {
                    throw new AssertionError(e);
                }
            }
            return Optional.of(member);
        }

        return Optional.empty();
    }
}
