package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.CookieManager;
import roomescape.JwtProvider;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final CookieManager cookieManager;
    public LoginMemberArgumentResolver(JwtProvider jwtProvider, MemberRepository memberRepository,CookieManager cookieManager) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.cookieManager=cookieManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = getRequest(webRequest);
        Cookie[] cookies = getCookies(request);

        String token = cookieManager.extractToken(cookies,"accessToken");

        Long memberId = jwtProvider.getMemberId(token);

        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        return new LoginMember(memberId, member.getName(), member.getEmail(), member.getPassword(),member.getRole());
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
}
