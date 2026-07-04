package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;

import java.util.Arrays;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletRequest request = getRequest(webRequest);
        Cookie[] cookies = getCookies(request);

        String token = extractToken(cookies);
        Long memberId = jwtProvider.getMemberId(token);
        Member member = memberDao.findById(memberId);
        if (member == null || !member.getRole.equeals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }

    private HttpServletRequest getRequest(NativeWebRequest nativeWebRequest) {
        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (httpServletRequest == null) {
            throw new IllegalStateException("request is not https");
        }
        return httpServletRequest;
    }

    private Cookie[] getCookies(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            throw new IllegalStateException("cookie is not exist");
        }
        return cookies;
    }

    private String extractToken(Cookie[] cookies) {
        String token = Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("token is not found in cookies"));
        return token;
    }
}
