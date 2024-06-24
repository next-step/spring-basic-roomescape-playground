package roomescape;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.infrastructure.JwtTokenDecoder;
import roomescape.infrastructure.TokenExtractor;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AdminHandlerInterceptor implements HandlerInterceptor {
    private MemberService memberService;

    public AdminHandlerInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = TokenExtractor.extractTokenFromCookie(request.getCookies());
        Long id = JwtTokenDecoder.decodeToken(token);
        Member member = memberService.findById(id);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }


}