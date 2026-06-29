package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.JwtProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.Arrays;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public LoginMemberArgumentResolver(JwtProvider jwtProvider, MemberDao memberDao) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new IllegalStateException("request is not https");
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new IllegalStateException("cookie is not exist");
        }
        String token = Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("token is not found in cookies"));

        Long memberId = jwtProvider.getMemberId(token);

        Member member = memberDao.findById(memberId);

        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
