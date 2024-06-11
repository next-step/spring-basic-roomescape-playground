package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.JwtUtils;
import roomescape.provider.CookieProvider;
import roomescape.provider.TokenProvider;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtUtils jwtUtils;

    public LoginMemberArgumentResolver(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private MemberService memberService;
    private TokenProvider tokenProvider;
    private CookieProvider cookieProvider;

    private MemberDao memberDao;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;

    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.getParameterType().equals(MemberResponse.class);

        return parameter.getParameterType().equals(Member.class);

    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        Long memberId = Long.valueOf(jwtUtils.extractSubject(token));

        return new MemberResponse(memberId);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";

        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = cookieProvider.extractTokenFromCookie(httpServletRequest.getCookies());
        String name = tokenProvider.getMemberFromToken(token);
        Member member = memberDao.findByName(name);

    }
}