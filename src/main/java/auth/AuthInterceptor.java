package auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

import java.util.Arrays;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    @Autowired
    public AuthInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue).orElseThrow(() -> new IllegalArgumentException("Missing token"));

        Member member = memberService.findByName(memberService.memberChecking(token).name());
        System.out.println("member.getRole() = " + member.getRole());
        System.out.println("member.getEmail() = " + member.getEmail());
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
