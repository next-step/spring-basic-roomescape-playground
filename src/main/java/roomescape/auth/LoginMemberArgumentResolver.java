package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.MemberInfo;
import roomescape.auth.exception.InvalidTokenException;
import roomescape.member.service.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginMemberArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie tokenCookie = WebUtils.getCookie(request, "token");
        if (tokenCookie == null || tokenCookie.getValue() == null) {
            throw new InvalidTokenException("Token is required.");
        }

        String token = tokenCookie.getValue();

        Long memberId = jwtTokenProvider.validateToken(token);
        MemberInfo memberInfo = MemberInfo.from(memberService.loadMember(memberId));

        return new LoginMember(memberInfo.id(), memberInfo.name(), memberInfo.email(), memberInfo.role());
    }
}
