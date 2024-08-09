package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.id())).body(member);
    }

    @PostMapping("/login")  // login -> 토큰 발급 요청 -> Cookie 응답
    public ResponseEntity<Void> login(@RequestBody TokenReqDto reqDto, HttpServletResponse response) {
        TokenResDto tokenResDto = memberService.createToken(reqDto);
        Cookie cookie = new Cookie("token", tokenResDto.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE,"application/json")
                .header("Keep-Alive", "timeout=60")
                .header(HttpHeaders.SET_COOKIE, "token=" + tokenResDto + "; Path=/; HttpOnly")
                .build();
    }

    /*
    요구사항:
    Cookie에서 토큰 정보를 추출하여
    멤버를 찾아 멤버 정보를 응답합니다.
     */
    @GetMapping("login/check")
    public ResponseEntity<String> getMemberInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = memberService.extractTokenFromCookie(cookies);
        MemberResponse memberInfo = memberService.getMemberFromToken(token);

        String dateHeader = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now());
        return ResponseEntity.status(HttpStatus.OK)
                .header("Connection", "keep-alive")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.DATE, dateHeader)
                .header("Keep-Alive", "timeout=60")
                .header("Transfer-Encoding", "chunked")
                .body(memberInfo.name());
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
