package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final TokenExtractor tokenExtractor;

    public AdminInterceptor(AuthService authService, TokenExtractor tokenExtractor) {
        this.authService = authService;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String token = tokenExtractor.extract(request);

        if (token.isBlank()) {
            throw new UnauthorizedException();
        }

        try {
            LoginMember loginMember = authService.findLoginMemberByToken(token);

            if (!authService.isAdmin(loginMember)) {
                throw new UnauthorizedException();
            }

            return true;
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }
}
