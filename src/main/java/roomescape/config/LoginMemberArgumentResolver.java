package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.api.JwtDecoder;
import roomescape.auth.AuthService;
import roomescape.member.Member;
import roomescape.util.CookieUtil;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private AuthService authService;

    public LoginMemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        /*
        커스텀 어노테이션 사용 시 사용할 수 있는 코드
        boolean hasLoginMemberAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
         */
        return Member.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie[] cookies = request.getCookies();
        String token = CookieUtil.extractTokenFromCookie(cookies);
        if(token == null){
            return null;
        }
       return authService.getLoginMemberWithToken(token);
    }
}
