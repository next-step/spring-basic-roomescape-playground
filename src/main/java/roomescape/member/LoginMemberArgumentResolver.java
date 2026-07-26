package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.JwtUtils;

//컨트롤러의 매개변수를 자동으로 만들어주는 클래스
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtUtils jwtUtils;

    public LoginMemberArgumentResolver(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }//컨트롤러의 매개변수가 member 타입이면 처리할 수 있다는 뜻

    @Override //Member객체를 실제로 만드는 함수
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(); //현재 HTTP 요청 가져오기
        String token = extractToken(request);

        if (token == null) {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }

        Claims claims = jwtUtils.parseToken(token);
        Long id = Long.parseLong(claims.getSubject());
        String role = claims.get("role", String.class);
        return new Member(id, role); // 필요한 id와 role만 가진 객체 반환
    }

    private String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
//브라우저가 보낸 JWT 쿠키를 읽어서 그 안에 저장된 회원 정보를 Member 객체로 복원한 뒤, 컨트롤러의 Member loginMember 매개변수에 자동으로
//넣어주는 역할
