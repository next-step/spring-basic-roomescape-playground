package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.Member;
import roomescape.member.MemberDao;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberDao memberDao;
    private final TokenService tokenService;

    public LoginMemberArgumentResolver(MemberDao memberDao, TokenService tokenService) {
        this.memberDao = memberDao;
        this.tokenService = tokenService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = extractTokenFromCookie(request.getCookies());
        Long memberId = tokenService.getMemberIdFromToken(token);
        Member member = memberDao.findById(memberId);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) return "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
