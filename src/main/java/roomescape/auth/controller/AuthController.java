package roomescape.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.constants.AuthConstants;
import roomescape.auth.util.CookieManager;
import roomescape.auth.security.JwtTokenProvider;
import roomescape.exception.LoginFailedException;
import roomescape.exception.UnauthorizedAccessException;
import roomescape.member.dto.Member;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.MemberService;

@RestController
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthController(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLoginStatus(HttpServletRequest request) {
        try {
            CookieManager cookieManager = new CookieManager(request.getCookies());
            String token = cookieManager.getValue(AuthConstants.AUTH_TOKEN_COOKIE);
            String userEmail = jwtTokenProvider.getEmailFromToken(token);
            Member member = memberService.findByEmail(userEmail);

            MemberResponse memberResponse = new MemberResponse(member.getId(), member.getName(), member.getEmail());
            return ResponseEntity.ok(memberResponse);
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
