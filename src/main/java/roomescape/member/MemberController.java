package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.JwtTokenProvider;

import java.net.URI;

@RestController
public class MemberController {
    private JwtTokenProvider jwtUtil;
    private MemberService memberService;

    public MemberController(MemberService memberService, JwtTokenProvider jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberRequest memberRequest, HttpServletResponse response){
        MemberResponse member = memberService.findMember(memberRequest.getEmail(),memberRequest.getPassword()); //멤버조회
        String token = memberService.createToken(member);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token=memberService.extractTokenFromCookie(cookies);

        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        MemberResponse member = memberService.findMemberById(memberId);

        return ResponseEntity.ok(member);
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