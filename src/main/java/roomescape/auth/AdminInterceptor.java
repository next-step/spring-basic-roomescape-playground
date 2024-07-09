package roomescape.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {

	private MemberService memberService;
	private final JwtUtils jwtUtils;

	public AdminInterceptor(JwtUtils jwtUtils, MemberService memberService) {
		this.jwtUtils = jwtUtils;
		this.memberService = memberService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		String token = jwtUtils.extractTokenFromCookie(request.getCookies());
		Member member = memberService.getMemberFromToken(token);
		if (member == null || !member.getRole().equals("ADMIN")) {
			response.setStatus(401);
			return false;
		}
		return true;
	}
}
