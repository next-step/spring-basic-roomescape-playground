package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.ApiException;
import roomescape.member.Member;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    public static Authorized findAuthorizedAnnotation(Method method) {
        return AnnotatedElementUtils.findMergedAnnotation(method, Authorized.class);
    }

    private final AuthorizationService authorizationService;

    public AuthorizationInterceptor(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod method) {
            authorizeMethod(request, method);
        }
        return true;

    }

    private void authorizeMethod(HttpServletRequest request, HandlerMethod method) {
        Authorized annotation = findAuthorizedAnnotation(method.getMethod());
        if (annotation == null) {
            return;
        }

        AuthorizedMember requestMember = authorizationService.tryAuthorizeRequest(request);
        if (requestMember == null) {
            throw ApiException.status(HttpStatus.UNAUTHORIZED);
        }

        authorizeRequest(annotation, requestMember);
    }

    private void authorizeRequest(Authorized annotation, AuthorizedMember requestMember) {
        List<Member.Role> roles = Arrays.asList(annotation.role());
        if (!roles.contains(requestMember.role())) {
            throw ApiException.status(HttpStatus.UNAUTHORIZED);
        }
    }
}
