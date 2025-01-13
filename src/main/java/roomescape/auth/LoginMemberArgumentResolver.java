package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.jwt.JwtProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public LoginMemberArgumentResolver(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookies(cookies);

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token not found in cookies");
        }

        if (jwtProvider.isValidToken(token)) {
            String email = jwtProvider.extractEmail(token); // 이메일 추출
            Member member = memberDao.findByEmailAndPassword(email, null); // 비밀번호는 검증 단계에서 사용하지 않음

            if (member == null) {
                throw new IllegalArgumentException("Member not found for email: " + email);
            }

            return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
        }
        throw new IllegalArgumentException("Invalid token");
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }
}