package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {

        @Autowired
        private MemberService memberService;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String token = memberService.extractTokenFromCookie(request.getCookies());
                Member member = memberService.extractMemberFromToken(token);

                if (member == null || !member.getRole().equals("ADMIN")) {
                        response.setStatus(401);
                        return false;
                }

                return true;
        }
}