package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
//이 클래스가 스프링 컴포넌트 스캔에 의해 빈으로 등록
@RequiredArgsConstructor
//lombok을 사용하여 final필드에 대해 생성자 자동으로 생성
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    //인터페이스는 특정 타입의 메서드 파라미터를 해석하고 주입할 수 있도록 도와준다.
    private final MemberService memberService;
    //MemberSerivce를 주입받는다. 토큰에서 멤버 정보를 추출하는데 사용
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }
    //메서드 파라미터가 LoginMember 타입인 경우에만 Argument Resolver가 사용되도록
    //MethodParameter객체를 검사하여 파라미터 타입이 LoginMember인지 확인, 일치하면 true반환

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = memberService.extractTokenFromCookie(httpServletRequest.getCookies());
        Member member = memberService.extractMemberFromToken(token);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
    //실제 파라미터 값을 해석하여 반환
    //NativeWebRequest객체에서 원본 HTTP요청 가져옴
    //요청의 쿠키에서 토큰 추출
    //추출한 토큰을 사용하여 MemberService를 통해 Member객체 가져온다
    //Member객체에서 필요한 정볼를 추출하여 LoginMember객체를 생성하고 반환
}
//스프링의 컨트롤러 메서드에서 LoginMember타입의 파라미터가 선언되었을 때, 해당 파라미테어 자동으로 현재 로그인한 사용자의 정보를 주입