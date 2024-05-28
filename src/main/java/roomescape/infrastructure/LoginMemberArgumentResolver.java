package roomescape.infrastructure;

import auth.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtUtils jwtUtils;

    public LoginMemberArgumentResolver(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    //매개변수로 넣어줄 값을 제공하는 메소드
    //토큰을 추출해 멤버를 찾는 것은 공통 로직
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //...
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token=jwtUtils.extractTokenFromCookie(request.getCookies()); //토큰 추출
        Long memberId = Long.valueOf(jwtUtils.extractSubject(token));

        return new LoginMember(memberId);
    }
}
