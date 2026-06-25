package roomescape.auth.config;

import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.member.DTO.MemberResponse;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        MemberResponse member = (MemberResponse) request.getAttribute("member");

        if (member == null || !"ADMIN".equals(member.getRole())) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}