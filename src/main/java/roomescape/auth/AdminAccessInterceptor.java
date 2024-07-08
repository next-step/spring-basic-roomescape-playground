package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import roomescape.member.MemberService;
import roomescape.member.ViewMemberResponse;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;

@Component
public class AdminAccessInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public AdminAccessInterceptor(JwtProvider jwtProvider, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Optional<String> token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue);

        if(token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String tokenValue = token.get();
        Claims claims = jwtProvider.getClaimsFromToken(tokenValue);
        String role = claims.get("role", String.class);
        ViewMemberResponse member = memberService.findMemberById(Long.parseLong(claims.getSubject()));

        if(member == null || !member.getRole().equalsIgnoreCase("admin")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}
