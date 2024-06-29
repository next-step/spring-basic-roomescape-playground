package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.instructure.JwtTokenProvider;

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
        MemberResponse member = memberService.findMember(memberRequest.getEmail(), memberRequest.getPassword());
        if (member == null) { //멤버 X
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = memberService.createToken(member);
        memberService.createCookie(response, token);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = memberService.extractTokenFromCookie(cookies);

        Long memberId = memberService.extractMemberIdFromToken(token);
        MemberResponse member = memberService.findMemberById(memberId);

        MemberResponse memberResponse = new MemberResponse(member.getId(), member.getName(), member.getEmail());
        return ResponseEntity.ok().body(memberResponse);
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