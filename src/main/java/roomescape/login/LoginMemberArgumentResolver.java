package roomescape.login;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {


    private JwtTokenProvider jwtTokenProvider;
    private MemberDao memberDao;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }
        System.out.println("token is = " + token);
        if (token != null) {
            Claims claims = jwtTokenProvider.getPayload(token);
            System.out.println("claims = " + claims);
            String email = String.valueOf(claims.get("email"));
            String password = String.valueOf(claims.get("password"));
            Member member = memberDao.findByEmailAndPassword(email, password);
            return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
        }
        return null;
    }
}
