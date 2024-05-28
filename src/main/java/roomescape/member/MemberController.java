package roomescape.member;

import auth.JwtUtils;
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
import roomescape.reservation.ReservationRequest;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;
    private JwtUtils jwtUtils;


    public MemberController(MemberService memberService, JwtUtils jwtUtils) {
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberRequest memberRequest, HttpServletResponse response){
        Member member = memberService.findMember(memberRequest.getEmail(),memberRequest.getPassword()); //멤버조회
        String token=jwtUtils.createToken(member); //조회된 멤버 정보로 토큰 생성
        jwtUtils.createCookie(response, token); //생성된 토큰으로 쿠키 생성해 response에 담기
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        String token=jwtUtils.extractTokenFromCookie(request.getCookies()); //토큰 추출
        Long memberId = Long.valueOf(jwtUtils.extractSubject(token));

        Member member = memberService.findById(memberId);
        MemberResponse memberResponse = new MemberResponse(member.getId(),member.getName(),member.getEmail(), member.getRole());
        return ResponseEntity.ok(memberResponse);
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
