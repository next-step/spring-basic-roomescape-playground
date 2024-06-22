package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
import roomescape.member.dto.MemberLoginCheckResponse;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@RestController
public class MemberController {
    public static final String TOKEN_NAME = "token";
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLoginRequest request,
                                HttpServletResponse response) {
        final String token = memberService.findMember(request); // 요청받은 email과 비밀번호로 사용자 조회 후, 토큰 발급

        Cookie cookie = new Cookie(TOKEN_NAME, token); // 해당 토큰을 담은 쿠키 생성
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .header("Keep-Alive", "timeout=60")
                .header("Content-Type","application/json")
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity loginCheck(HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();

        // 쿠키에 있는 항목들 중 토큰 값 추출
        String token = null;
        for (final Cookie cookie : cookies) {
            final String name = cookie.getName();
            if (name.equals(TOKEN_NAME)) {
                token = cookie.getValue();
                break;
            }
        }

        // 해당 토큰에서 인증 정보 조회
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(
                        "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        // 토큰에 담긴 name을 추출
        final String name = claims.get("name", String.class);
        return ResponseEntity.ok().body(new MemberLoginCheckResponse(name));
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
