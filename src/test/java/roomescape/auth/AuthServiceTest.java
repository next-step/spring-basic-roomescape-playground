package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@SpringBootTest
class AuthServiceTest {
    private final String originSecretKey = "ThisIsATestKeyForJsonWebTokenProvider";
    private final long originValidity = 6000;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            new JwtProperties(originSecretKey, originValidity),
            new SystemTimeProvider());

    private final Member member = new Member(1L, "test", "test@email.com", "ADMIN");

    @Autowired
    private MemberRepository memberRepository;
    private final AuthService authService = new AuthService(jwtTokenProvider, memberRepository);

    @Test
    void 토큰의_키가_존재하지_않는_경우_토큰_정보_조회에_실패한다() {
        //given
        Date now = new SystemTimeProvider().now();
        Date validity = new Date(now.getTime() + originValidity);

        String token = Jwts.builder()
                .setSubject(member.getEmail())
                .claim("name", member.getName())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(originSecretKey.getBytes()))
                .compact();

        //when, then
        assertThatThrownBy(() -> authService.createAuthentication(token))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
