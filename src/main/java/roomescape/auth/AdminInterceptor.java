package roomescape.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.jwt.JwtUtil;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {

	private MemberService memberService;

	public AdminInterceptor(MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		String token = JwtUtil.extractTokenFromCookie(request.getCookies());
		Member member = memberService.getMemberFromToken(token);
		if (member == null || !member.getRole().equals("ADMIN")) {
			response.setStatus(401);
			return false;
		}
		return true;
	}
}
