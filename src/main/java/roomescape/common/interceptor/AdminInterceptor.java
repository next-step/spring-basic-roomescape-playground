package roomescape.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.dto.response.MemberTokenDto;
import roomescape.auth.service.TokenService;
import roomescape.auth.util.CookieProvider;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {
	private CookieProvider cookieProvider;
	private TokenService tokenService;
	private MemberService memberService;

	public AdminInterceptor(CookieProvider cookieProvider, TokenService tokenService, MemberService memberService) {
		this.cookieProvider = cookieProvider;
		this.tokenService = tokenService;
		this.memberService = memberService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		Cookie[] cookies = request.getCookies();
		String token = cookieProvider.extractTokenFromCookie(cookies)
			.orElse(null);

		if (token == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return false;
		}

		MemberTokenDto memberTokenDto = tokenService.extractMemberResponseFromToken(token);
		Member member = memberService.findMemberByName(memberTokenDto.name());
		if (member == null || !member.getRole().equals("ADMIN")) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		return true;
	}
}
