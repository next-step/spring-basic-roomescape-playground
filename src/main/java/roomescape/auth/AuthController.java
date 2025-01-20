package roomescape.auth;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private static final String TOKEN_COOKIE = "token";
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            String token = authService.createToken(loginRequest.email(), loginRequest.password());
            Cookie tokenCookie = createTokenCookie(token);
            response.addCookie(tokenCookie);
            return ResponseEntity.ok().build();

        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @GetMapping("/login/check")
    public ResponseEntity check(@CookieValue(name = TOKEN_COOKIE) String token) {
        try {
            Map<String, Object> claims = authService.extractClaims(token);
            return ResponseEntity.ok().body(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    private Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie(TOKEN_COOKIE, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
