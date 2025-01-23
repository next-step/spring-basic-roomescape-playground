package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor authorizationExtractor;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider, AuthorizationExtractor authorizationExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = authorizationExtractor.extract(request);
        Map<String, Object> claims = jwtTokenProvider.getClaims(token);
        LoginMember member = LoginMember.fromClaims(claims);

        if (member == null || !member.role().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
