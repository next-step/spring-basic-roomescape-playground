package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.context.request.NativeWebRequest;
import roomescape.auth.configuration.LoginMemberArgumentResolver;
import roomescape.auth.util.JwtPayload;
import roomescape.auth.util.JwtUtil;
import roomescape.member.MemberInfo;

public class LoginMemberArgumentResolverTest {

    JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
    LoginMemberArgumentResolver resolver = new LoginMemberArgumentResolver(jwtUtil);

    @Test
    @DisplayName("유효한 토큰이 있으면 MemberInfo를 반환한다.")
    void resolveArgumentValidTokenReturnsMemberInfo() {
        Cookie[] cookies = {new Cookie("token", "token")};
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getCookies()).thenReturn(cookies);
        NativeWebRequest webRequest = Mockito.mock(NativeWebRequest.class);
        Mockito.when(webRequest.getNativeRequest()).thenReturn(req);

        Mockito.when(jwtUtil.parseToken("token")).thenReturn(new JwtPayload("브라운", "ADMIN"));

        Object result = resolver.resolveArgument(null, null, webRequest, null);
        assertThat(result).isInstanceOf(MemberInfo.class);
        MemberInfo info = (MemberInfo) result;
        assertThat(info.name()).isEqualTo("브라운");
        assertThat(info.role()).isEqualTo("ADMIN");
    }
}
