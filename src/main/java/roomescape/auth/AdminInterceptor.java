package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.api.JwtDecoder;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.util.CookieUtil;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private MemberService memberService;

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String token = CookieUtil.extractTokenFromCookie(request.getCookies());
            Long id = JwtDecoder.decodeJwtToken(token);
            Member member = memberService.findById(id);

            if (member == null) {
                throw new NotFoundException("유저를 찾을 수 없습니다.");
            } else if (!member.getRole().equals("ADMIN")) {
               throw new UnauthorizedException("관리자 회원이 아닙니다.");
            }

            return true;
        } catch (NotFoundException | UnauthorizedException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
            return false;
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("예상치 못한 오류가 발생했습니다.");
            return false;
        }
    }
}
