package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.UnauthorizedException;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    // (1) LoginMember 타입인지 검사
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    // (3) 1에서 통과 후, 로그인 정보 검증 후 LoginMember 객체 반환
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();

        // 1) 쿠키가 존재하는지 검사
        if (cookies == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        // 2) 토큰이 비어있는지(로그인 상태가 맞는지) 검사
        String token = extractTokenFromCookie(cookies);
        if (token.isEmpty()) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Member member = memberService.findMemberByToken(token);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    // (2) 브라우저가 보낸 여러 쿠키(배열) 중 이름이 "token"인 쿠키를 찾는다
    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
