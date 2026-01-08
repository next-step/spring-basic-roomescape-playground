package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.ApiError;
import roomescape.util.JwtUtil;

import java.io.IOException;

public class AdminAuthInterceptor implements HandlerInterceptor {

    private final String secretKey;

    public AdminAuthInterceptor(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = JwtUtil.extractTokenFromCookies(request.getCookies());
        if (token.isEmpty()) {
			writeError(response, ApiError.UNAUTHORIZED_MISSING_TOKEN);
            return false;
        }

        try {
            Claims claims = JwtUtil.parseClaims(token, secretKey);
            String role = claims.get("role", String.class);
            if (!"ADMIN".equals(role)) {
				writeError(response, ApiError.FORBIDDEN_ADMIN_ONLY);
                return false;
            }
            return true;
		} catch (ExpiredJwtException e) {
			writeError(response, ApiError.UNAUTHORIZED_EXPIRED_TOKEN);
			return false;
        } catch (Exception e) {
			writeError(response, ApiError.UNAUTHORIZED_INVALID_TOKEN);
            return false;
        }
    }

	private void writeError(HttpServletResponse response, ApiError apiError) {
		response.setStatus(apiError.getHttpStatus().value());
		response.setContentType("application/json;charset=UTF-8");
		try {
			String payload = "{\"code\":" + apiError.getCode() + ",\"message\":\"" + apiError.getMessage() + "\"}";
			response.getWriter().write(payload);
		} catch (IOException ignored) {
		}
	}
}


