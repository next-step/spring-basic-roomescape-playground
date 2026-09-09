package roomescape.member.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.LoginMember;
import roomescape.member.provider.LoginMemberProvider;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.exception.MemberException;
import roomescape.member.annotation.AdminOnly;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final LoginMemberProvider loginMemberProvider;

    public AdminInterceptor(LoginMemberProvider loginMemberProvider) {
        this.loginMemberProvider = loginMemberProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        if (!requiresAdmin(handler)) {
            return true;
        }

        LoginMember member = loginMemberProvider.getLoginMember(request);
        if (!"ADMIN".equals(member.role())) {
            throw new MemberException(MemberErrorCode.ADMIN_REQUIRED);
        }

        return true;
    }

    private boolean requiresAdmin(Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return false;
        }

        return handlerMethod.hasMethodAnnotation(AdminOnly.class)
                || AnnotatedElementUtils.hasAnnotation(
                        handlerMethod.getBeanType(),
                        AdminOnly.class
                );
    }
}
