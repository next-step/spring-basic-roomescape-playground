package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.MemberService;
import roomescape.member.Role;
import roomescape.member.dto.LoginMember;


@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {


    private MemberService memberService;

    public AuthInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }



    @Override
    public boolean  preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        LoginMember member = memberService.checkLogin(cookies);

        if (member == null || !member.getRole().equals(Role.ADMIN)) {
            response.sendError(401, "You are not allowed to access this resource");
            return false;
        }
        return true;
    }
}
