package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Key;
import java.util.Map;

@RestController
public class MemberController {
    private final MemberService memberService;
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Map<String, String> params, HttpServletResponse response) {
        String email = params.get("email");
        String password = params.get("password");

        ViewMemberResponse viewMemberResponse = memberService.findMemberByEmailAndPassword(email, password);

        if (viewMemberResponse != null) {
            String token = Jwts.builder()
                    .setSubject(viewMemberResponse.getId().toString())
                    .claim("name", viewMemberResponse.getName())
                    .claim("role", viewMemberResponse.getRole())
                    .signWith(KEY)
                    .compact();

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String token = extractTokenFromCookie(cookies);
            if (!token.isEmpty()) {
                try {
                    Long memberId = Long.valueOf(Jwts.parserBuilder()
                            .setSigningKey(KEY)
                            .build()
                            .parseClaimsJws(token)
                            .getBody().getSubject());

                    ViewMemberResponse viewMemberResponse = memberService.findMemberById(memberId);
                    MemberResponse memberResponse = new MemberResponse(viewMemberResponse.getId(), viewMemberResponse.getName(), viewMemberResponse.getEmail());
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
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
