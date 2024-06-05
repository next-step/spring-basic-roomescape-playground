package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MemberController {
    private MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse response) {

        String email = params.get("email");
        String password = params.get("password");

        ViewMemberResponse viewMemberResponse = memberService.findMemberByEmailAndPassword(email, password);

        String assessToken = Jwts.builder()
                .setSubject(viewMemberResponse.getId().toString())
                .claim("name", viewMemberResponse.getName())
                .claim("role", viewMemberResponse.getRole())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();

        Cookie cookie = new Cookie("token", assessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String token = extractTokenFromCookie(cookies);
            if (!token.isEmpty()) {
                try {
                    Long memberId = Long.valueOf(Jwts.parserBuilder()
                            .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                            .build()
                            .parseClaimsJws(token)
                            .getBody().getSubject());

                    ViewMemberResponse memberResponse = memberService.findMemberById(memberId);
                    return ResponseEntity.ok(memberResponse);
                } catch (Exception e) {
                    return ResponseEntity.status(401).build();
                }
            }
        }
        return ResponseEntity.status(401).build();
    }
    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
