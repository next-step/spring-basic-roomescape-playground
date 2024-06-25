package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.MemberService;
import roomescape.member.ViewMemberResponse;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    public LoginMemberArgumentResolver(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = LoginMember.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    System.out.println("==========");
                    System.out.println("token == " + token);
                    System.out.println("==========");

                    Claims claims = jwtProvider.getClaimsFromToken(token);
                    if (claims != null && claims.getSubject() != null) {
                        System.out.println("claims == " + claims.getSubject());
                        Long memberId = Long.parseLong(claims.getSubject());
                        ViewMemberResponse member = memberService.findMemberById(memberId);

                        if (member != null) {
                            System.out.println("==========");
                            System.out.println("member == " + member.getEmail());
                            System.out.println("==========");

                            return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
                        } else {
                            throw new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다.");
                        }
                    } else {
                        throw new IllegalArgumentException("토큰에서 유효한 클레임을 추출할 수 없습니다.");
                    }
                }
            }
        }
        throw new IllegalArgumentException("유효한 인증 정보가 없습니다.");
    }
}
