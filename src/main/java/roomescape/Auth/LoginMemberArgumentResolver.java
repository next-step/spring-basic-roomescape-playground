package roomescape.Auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
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
        String token=memberService.extractTokenFromCookie(request.getCookies()); //토큰 추출
        Member member = memberService.findByToken(token);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}