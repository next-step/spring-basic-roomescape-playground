package roomescape.auth;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

	private final MemberService memberService;
	private final JwtUtils jwtUtils;

	public LoginMemberArgumentResolver(MemberService memberService, JwtUtils jwtUtils) {
		this.memberService = memberService;
		this.jwtUtils = jwtUtils;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(LoginMember.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		Cookie[] cookies = request.getCookies();
		String token = jwtUtils.extractTokenFromCookie(cookies);
		if (token == null) {
			return null;
		}
		Member member = memberService.getMemberFromToken(token);
		return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
	}
}
