package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jwt.JwtService;
import roomescape.member.Member;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    private LoginService loginService;
    private JwtService jwtService;

    public LoginController(LoginService loginService, JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String token = loginService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        Cookie cookie = loginService.createAuthCookie(token);
        response.addCookie(cookie);
        Map<String, Object> responseBody = loginService.createLoginResponse(token);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> checkLogin(@CookieValue(name = "token", required = true) String token) {
        Member member = jwtService.getMemberFromToken(token);

        Map<String, String> response = new HashMap<>();
        response.put("name", member.getName());
        return ResponseEntity.ok(response);
    }
}
