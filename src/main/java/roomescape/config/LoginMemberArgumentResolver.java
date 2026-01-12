package roomescape.config;

import roomescape.auth.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundDataException;
import roomescape.member.LoginMember;
import roomescape.util.CookieUtil;

@Slf4j
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtUtils jwtUtils;

    public LoginMemberArgumentResolver(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = CookieUtil.extractToken(request.getCookies());

        if (token == null) {
            log.warn("로그인이 필요한 요청: uri={}", request.getRequestURI());
            throw new NotFoundDataException(ErrorMessage.LOGIN_REQUIRED.getMessage());
        }

        try {
            Long id = jwtUtils.getId(token);
            String name = jwtUtils.getName(token);
            String email = jwtUtils.getEmail(token);
            String role = jwtUtils.getRole(token);

            log.debug("로그인 사용자 인증 성공: memberId={}, uri={}", id, request.getRequestURI());
            return new LoginMember(id, name, email, role);
        } catch (Exception e) {
            log.error("토큰 인증 실패: uri={}, error={}", request.getRequestURI(), e.getMessage());
            throw new NotFoundDataException(ErrorMessage.INVALID_AUTH_INFO.getMessage());
        }
    }
}
