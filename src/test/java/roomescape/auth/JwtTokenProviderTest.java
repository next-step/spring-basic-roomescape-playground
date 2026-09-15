package roomescape.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import roomescape.member.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtTokenProviderTest {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
    private final Member member = new Member(1L, "어드민", "admin@email.com", "ADMIN");

    @Test
    void 유효한_토큰이면_로그인_정보를_반환한다() {
        String token = jwtTokenProvider.createToken(member);
        LoginMember loginMember = jwtTokenProvider.getLoginMember(token);

        assertThat(loginMember.getName()).isEqualTo("어드민");
    }

    @Test
    void 만료된_토큰이면_예외가_발생한다() {
        String expiredToken = jwtTokenProvider.createToken(member, -1000);

        assertThatThrownBy(() -> jwtTokenProvider.getLoginMember(expiredToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void 형식이_깨진_토큰이면_null을_반환한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("token", "not-a-jwt")});

        LoginMember loginMember = jwtTokenProvider.resolveLoginMember(request);

        assertThat(loginMember).isNull();
    }

    @Test
    void 서명이_일치하지_않는_토큰이면_null을_반환한다() {
        String wrongKeySignedToken = Jwts.builder()
                .setSubject("1")
                .claim("name", "어드민")
                .claim("role", "ADMIN")
                .signWith(Keys.hmacShaKeyFor("this-is-a-completely-different-secret-key-value".getBytes()))
                .compact();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("token", wrongKeySignedToken)});

        LoginMember loginMember = jwtTokenProvider.resolveLoginMember(request);

        assertThat(loginMember).isNull();
    }

    @Test
    void 만료된_토큰이면_null을_반환한다() {
        String expiredToken = jwtTokenProvider.createToken(member, -1000);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("token", expiredToken)});

        LoginMember loginMember = jwtTokenProvider.resolveLoginMember(request);

        assertThat(loginMember).isNull();
    }
}
