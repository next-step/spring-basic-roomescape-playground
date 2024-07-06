package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final AuthorizationProvider authorizationProvider;

    public AdminInterceptor(AuthorizationProvider authorizationProvider) {
        this.authorizationProvider = authorizationProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        Optional<String> token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue);
        if (token.isEmpty()) {
            response.setStatus(401);
            return false;
        }

        MemberCredential memberCredential = new MemberCredential(token.get());
        MemberAuthContext memberAuthContext = authorizationProvider.parseCredential(memberCredential);
        if (!memberAuthContext.role().equalsIgnoreCase("admin")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
