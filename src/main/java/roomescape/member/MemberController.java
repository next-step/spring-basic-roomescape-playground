package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
//이 클래스가 RestFull웹 서비스의 컨트롤러
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    //MemberService 주입받아 사용 <회원 생성, 로그인, 로그아웃>등의 로직 처리
    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }
    //요청 바디에서 MemberRequest객체를 받아 새로운 회원을 생성
    //생성된 회원 정보를 포함한 MemberResponse객체를 반환
    //응답에는 생성된 리소스의 URI를 포함

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = memberService.createToken(loginRequest);
        Cookie cookie = memberService.createCookie(accessToken);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
    //요청 바디에서 LoginRequest객체를 받아 로그인 처리
    //로그인 성공시 엑세스 토큰을 생성, 이를 쿠키에 저장하여 클라이언트에 반환
    //응답은 HTTP 200 OK 상태를 반환

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(HttpServletRequest request) {
        String token = memberService.extractTokenFromCookie(request.getCookies());
        String name = memberService.extractNameFromToken(token);
        LoginCheckResponse loginCheckResponse = new LoginCheckResponse(name);
        return ResponseEntity.ok().body(loginCheckResponse);
    }

    //요청의 크키에서 토큰을 추출하고, JWT파서를 사용하여 토큰의 유효성을 검사한 후 사용자 이름을 추출
    //loginCheckResponse객체에 사용자 이름을 담아 반환
    //응답은 HTTP200 OK 상태와 함꼐 LoginCheckResponse객체를 반환

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
    //클라이언트의 토큰 쿠키를 삭제하여 로그아웃을 처리
    //쿠키의 값을 빈 문자열로 설정, Max-age를 0으로 설정하여 쿠키를 만료
    //응답은 HTTP 200 Ok 상태를 반환
}