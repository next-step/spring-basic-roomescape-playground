package roomescape.auth;

import auth.JwtUtilsV4;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.InvalidTokenException;
import roomescape.member.Member;
import roomescape.member.MemberService;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtUtilsV4 jwtUtils;
    private final CookieValueExtractor cookieValueExtractor;


    public LoginMemberArgumentResolver(MemberService memberService, JwtUtilsV4 jwtUtils,
                                       CookieValueExtractor cookieValueExtractor) {
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
        this.cookieValueExtractor = cookieValueExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {

            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            String token = cookieValueExtractor.extractToken(request.getCookies());
            if (token == null) {
                throw new InvalidTokenException();
            }

            Claims claims = jwtUtils.getClaims(token);
            Long memberId = Long.valueOf(claims.getSubject());
            Member member = memberService.getMemberById(memberId);

            return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());

        } catch (JwtException | NullPointerException | NumberFormatException e) {
            throw new InvalidTokenException();
        }
    }

}
