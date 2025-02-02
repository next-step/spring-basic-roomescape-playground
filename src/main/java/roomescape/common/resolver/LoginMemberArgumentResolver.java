package roomescape.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.auth.dto.response.MemberTokenDto;
import roomescape.auth.service.TokenService;
import roomescape.auth.util.CookieProvider;
import roomescape.auth.dto.response.LoginMember;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

	private MemberService memberService;
	private TokenService tokenService;
	private CookieProvider cookieProvider;

	public LoginMemberArgumentResolver(MemberService memberService, TokenService tokenService, CookieProvider cookieProvider) {
		this.memberService = memberService;
		this.tokenService = tokenService;
		this.cookieProvider = cookieProvider;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(LoginMember.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
		Cookie[] cookies = httpServletRequest.getCookies();
		String token = cookieProvider.extractTokenFromCookie(cookies)
			.orElseThrow(() -> new IllegalArgumentException("쿠키에 토큰이 존재하지 않습니다."));

		MemberTokenDto memberTokenDto = tokenService.extractMemberResponseFromToken(token);
		Member member = memberService.findMemberByName(memberTokenDto.name());
		return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
	}
}
