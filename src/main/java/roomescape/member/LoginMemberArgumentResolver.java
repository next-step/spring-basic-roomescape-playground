package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) { //로그인한 회원정보의 필요여부 판단
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();

        String token = extractTokenFromCookie(cookies);

        MemberResponse memberResponse = memberService.findMemberByToken(token);

        return new LoginMember(
                memberResponse.getId(),
                memberResponse.getName(),
                memberResponse.getEmail(),
                "USER"
        );
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException("쿠키가 존재하지 않아 인증할 수 없습니다.");
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException("인증 토큰(token) 쿠키를 찾을 수 없습니다.");
    }
}
