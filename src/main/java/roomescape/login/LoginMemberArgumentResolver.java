package roomescape.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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

    private final String secretKey;
    private final MemberDao memberDao;

    public LoginMemberArgumentResolver(String secretKey, MemberDao memberDao) {
        this.secretKey = secretKey;
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return LoginMember.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        if (request == null || request.getCookies() == null) {
            return null;
        }

        String token = "";
        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
        if (token.isBlank()) {
            return null;
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long memberId = Long.valueOf(claims.getSubject());
        Member member  = memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원을 찾을 수 없습니다"));

        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
