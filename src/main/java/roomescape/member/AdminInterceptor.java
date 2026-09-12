package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = TokenExtractor.extract(request.getCookies());
        Member member = memberService.findByToken(token);

        if (!"ADMIN".equals(member.getRole())) {
            throw new BusinessException(ErrorCode.NOT_ADMIN);
        }
        return true;
    }
}
