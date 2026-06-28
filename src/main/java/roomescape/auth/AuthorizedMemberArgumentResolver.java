package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthorizedMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthorizationService authorizationService;

    public AuthorizedMemberArgumentResolver(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterType() == AuthorizedMember.class) {
            if (AuthorizationInterceptor.findAuthorizedAnnotation(parameter.getMethod()) == null) {
                System.err.print("AuthorizedMember argument를 받으려면 @Authorized를 포함해야 합니다: ");
                System.err.println(parameter.getMethod());
                return false;
            }

            return true;
        }

        if (parameter.getParameterType() == Optional.class) {
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

        Class<?> parameterType = parameter.getParameterType();

        if (parameterType == AuthorizedMember.class) {
            return resolveAuthorizedMember(request);
        }

        if (parameterType == Optional.class) {
            return resolveAuthorizedMemberOptional(request);
        }

        throw new AssertionError("unexhaustive code for type: " + parameter);
    }

    private AuthorizedMember resolveAuthorizedMember(HttpServletRequest request) {
        Optional<AuthorizedMember> member = resolveAuthorizedMemberOptional(request);
        return member.orElseThrow(() -> new AssertionError("@Authorized 없이 AuthorizedMember을 사용하지 마세요."));
    }

    private Optional<AuthorizedMember> resolveAuthorizedMemberOptional(HttpServletRequest request) {
        AuthorizedMember member = authorizationService.tryAuthorizeRequest(request);
        return Optional.ofNullable(member);
    }
}
