package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.exception.ExceptionMessage;
import roomescape.exception.UnAuthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.auth.JwtTokenManager.ACCESS_TOKEN_EXP;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtTokenManagerTest {

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Test
    void 액세스_토큰을_파싱할_수_있다() {
        // given
        Member member = new Member(1L, "멤버", "member@email.com", Role.USER);
        String accessToken = jwtTokenManager.createAccessToken(member);
        // when
        long resultOfParseToken = jwtTokenManager.parseToken(accessToken);
        // then
        assertThat(resultOfParseToken).isEqualTo(member.getId());
    }

    @Test
    void 만료된_토큰을_파싱하면_예외를_반환한다() {
        // given
        Member member = new Member(1L, "멤버", "member@email.com", Role.USER);
        String token = createExpiredToken(member);
        // when & then
        assertThatThrownBy(() -> jwtTokenManager.parseToken(token))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage(ExceptionMessage.EXPIRED_TOKEN.getMessage());
    }

    @Test
    void 잘못된_서명_토큰을_파싱하면_예외를_반환한다() {
        // given
        Member member = new Member(1L, "멤버", "member@email.com", Role.USER);
        String invalidToken = createInvalidToken(member);
        // when & then
        assertThatThrownBy(() -> jwtTokenManager.parseToken(invalidToken))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage(ExceptionMessage.INVALID_TOKEN.getMessage());
    }

    private String createExpiredToken(Member member) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() - ACCESS_TOKEN_EXP);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    private String createInvalidToken(final Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor("wrong-secret-keyYDfhiadomlkasiuhuj".getBytes()))
                .compact();
    }
}
