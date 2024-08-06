package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;

public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResponseEntity<String> authCheck = checkAuthentication(request);
        Cookie[] cookies = request.getCookies();
        if (authCheck != null) {
            handleResponseEntity(response, authCheck);
            return false;
        }

        LoginResponse loginResponse = getLoginResponse(request);

        if (loginResponse == null) {
            ResponseEntity<String> loginRedirect = ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "/login")
                    .body("유저를 찾을 수 없습니다. 로그인 페이지로 이동합니다.");
            handleResponseEntity(response, loginRedirect);
            return false;
        }
        if ("ADMIN".equals(loginResponse.getRole())) {
            return true; // 관리자 페이지 접근 허용
        } else {
            ResponseEntity<String> accessDenied = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.LOCATION, "/access-denied")
                    .body("관리자 페이지입니다. 접근 권한이 없습니다.");
            handleResponseEntity(response, accessDenied);
            return false;
        }
    }

    private ResponseEntity<String> checkAuthentication(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "/login")
                    .body("쿠키를 찾을 수 없습니다. 로그인 페이지로 이동합니다.");
        }

        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (tokenCookie == null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "/login")
                    .body("토큰을 찾을 수 없습니다. 로그인 페이지로 이동합니다.");
        }
        return null;
    }

    private LoginResponse getLoginResponse(HttpServletRequest request) {
        Cookie tokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (tokenCookie == null) {
            return null;
        }

        return authService.findUserByToken(tokenCookie.getValue());
    }

    private void handleResponseEntity(HttpServletResponse response, ResponseEntity<String> entity) throws IOException, IOException {
        response.setStatus(entity.getStatusCodeValue());
        response.setHeader(HttpHeaders.LOCATION, entity.getHeaders().getFirst(HttpHeaders.LOCATION));
        response.getWriter().write(entity.getBody());
    }
}
