package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.auth.configuration.AdminHandlerInterceptor;
import roomescape.auth.util.JwtPayload;
import roomescape.auth.util.JwtUtil;

public class AdminHandlerInterceptorTest {

    JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
    AdminHandlerInterceptor interceptor = new AdminHandlerInterceptor(jwtUtil);

    @Test
    @DisplayName("token이 없으면 401 Unauthorized를 반환한다.")
    void preHandleWithoutTokenReturnsUnauthorized() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getCookies()).thenReturn(null);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        boolean result = interceptor.preHandle(request, response, new Object());
        assertThat(result).isFalse();
        Mockito.verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("admin이 아니면 401 Unauthorized를 반환한다.")
    void preHandleNotAdminReturnsUnauthorized() throws Exception {
        Cookie[] cookies = {new Cookie("token", "token")};
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getCookies()).thenReturn(cookies);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(jwtUtil.parseToken("token")).thenReturn(new JwtPayload("유저", "USER"));

        boolean result = interceptor.preHandle(request, response, new Object());
        assertThat(result).isFalse();
        Mockito.verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("admin이면 true를 반환한다.")
    void preHandleAdminReturnsTrue() throws Exception {
        Cookie[] cookies = {new Cookie("token", "token")};
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getCookies()).thenReturn(cookies);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(jwtUtil.parseToken("token")).thenReturn(new JwtPayload("브라운", "ADMIN"));

        boolean result = interceptor.preHandle(request, response, new Object());
        assertThat(result).isTrue();
    }
}
