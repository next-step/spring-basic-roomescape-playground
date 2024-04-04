package roomescape.golbal;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.provider.CookieProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.provider.TokenProvider;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private MemberDao memberDao;
    private TokenProvider tokenProvider;
    private CookieProvider cookieProvider;

    public LoginMemberArgumentResolver(MemberDao memberDao, TokenProvider tokenProvider,
        CookieProvider cookieProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
        this.cookieProvider = cookieProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Cookie[] cookies = webRequest.getNativeRequest(HttpServletRequest.class).getCookies();
        String token = cookieProvider.extractTokenFromCookie(cookies);
        String name = tokenProvider.getMemberNameFromToken(token);
        Member member = memberDao.findByName(name);
        return new Member(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
