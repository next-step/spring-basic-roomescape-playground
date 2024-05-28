package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
//어노테이션 사용하여 스프링 빈으로 등록, 필요한 생성자 자동 생성
public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
//memberservice주입받아 사용, 생성시 반드시 초기화
    //특정 요청이 컨트롤러에 도달하기 전에 처리할 로직 정의
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = memberService.extractTokenFromCookie(request.getCookies());
        //request 객체에서 쿠키를 가져와 memberservice의  extractTokenFromCookie메소드 호출하여 토큰 추출
        Member member = memberService.extractMemberFromToken(token);
        //추출한 토큰을 이용해 해당 메소드를 호출하여 Member 객체를 가져온다

        if (member == null || !member.getRole().equals("ADMIN")) {
            //member가 null이거나 memeber의 역할이 ADMIN이 아닌 경우 확인
            response.setStatus(401);
            return false;
            //if 조건일 경우 응답상태 401로 설정 false 반환하여 요청 처리 충단
        }

        return true;
        //true반환 요청 처리 계속 진행
    }
    //관리자가 아닌 사용자가 관리자 페이지에 접근하려고 할 때 접근을 차단하는 역할
    //MemberService를 통해 사용자의 토큰을 추출하고, 해당 사용자가 관리자인지 확인하는 과정 거침
}
