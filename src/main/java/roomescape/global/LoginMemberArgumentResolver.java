package roomescape.global;

import static roomescape.auth.CookiesUtils.extractTokenFromCookie;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.auth.JwtProvider;
import roomescape.member.LoginMember;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public LoginMemberArgumentResolver(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        String name = nativeRequest.getParameter("name");
        if (name == null) {
            Cookie[] cookies = nativeRequest.getCookies();
            String token = extractTokenFromCookie(cookies);

            name = jwtProvider.getNameFromToken(token);
        }

        MemberResponse member = memberService.findMemberByName(name);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
