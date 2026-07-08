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
import roomescape.exception.TokenNotFoundException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

import java.util.Arrays;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public LoginMemberArgumentResolver(JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = getRequest(webRequest);
        Cookie[] cookies = getCookies(request);

        String token = extractToken(cookies,"accessToken");

        Long memberId = jwtProvider.getMemberId(token);

        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    private HttpServletRequest getRequest(NativeWebRequest nativeWebRequest) {
        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (httpServletRequest == null) {
            throw new IllegalStateException("request is not https");
        }
        return httpServletRequest;
    }

    private Cookie[] getCookies(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            throw new IllegalStateException("cookie is not exist");
        }
        return cookies;
    }

    private String extractToken(Cookie[] cookies,String cookieName) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(TokenNotFoundException::new);
    }

}
