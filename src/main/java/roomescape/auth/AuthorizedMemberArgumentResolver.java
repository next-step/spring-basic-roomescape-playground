package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
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
        if(parameter.getParameterType() == AuthorizedMember.class) return true;

        if(parameter.getParameterType() == Optional.class) {
            ResolvableType type = ResolvableType.forMethodParameter(parameter);
            return type.getGeneric(0).resolve() == AuthorizedMember.class;
        }

        return false;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        if (parameter.getParameterType() == AuthorizedMember.class) {
            return resolveAuthorizedMember(request);
        }

        if(parameter.getParameterType() == Optional.class) {
            return resolveAuthorizedMemberOptional(request);
        }

        throw new IllegalStateException("unexhaustive code for type: " + parameter);
    }

    private AuthorizedMember resolveAuthorizedMember(HttpServletRequest request) {
        Optional<AuthorizedMember> member = resolveAuthorizedMemberOptional(request);
        return member.orElseThrow(() -> new ErrorResponseException(HttpStatus.UNAUTHORIZED));
    }

    private Optional<AuthorizedMember> resolveAuthorizedMemberOptional(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("token")) {
                continue;
            }

            AuthToken token = new AuthToken(cookie.getValue());
            return Optional.of(authTokenProvider.parseSessionToken(token));
        }

        return Optional.empty();
    }
}
