package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {
        String password = loginRequest.get("password");
        String email = loginRequest.get("email");
        String token = loginService.login(email, password);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<Map<String, String>> checkLogin(@CookieValue("token") String token) {
        Map<String, String> userInfo = loginService.getUserInfoFromToken(token);
        return ResponseEntity.ok(userInfo);
    }
}
