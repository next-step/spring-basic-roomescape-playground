package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final MemberDao memberDao;
    private final MemberService memberService;

    public AdminInterceptor(MemberDao memberDao, MemberService memberService) {
        this.memberDao = memberDao;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long memberId = memberService.checkLogin(request).getId();
        Member member = memberDao.findById(memberId);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}