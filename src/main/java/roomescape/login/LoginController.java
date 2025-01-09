package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.MemberResponse;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){

        String token = loginService.login(loginRequest.getEmail(), loginRequest.getPassword());
        //쿠키 생성
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check") //쿠키 조회
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookies(cookies);
        MemberResponse memberResponse = loginService.validateToken(token);

        return ResponseEntity.ok(memberResponse);
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie: cookies){
            if ("token".equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return "";
    }
}
