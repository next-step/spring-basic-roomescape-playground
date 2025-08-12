package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.util.JwtPayload;
import roomescape.auth.util.JwtUtil;
import roomescape.member.Member;

public class JwtUtilTest {

    String secret = "securesecuresecuresecuresecure12345678";
    JwtUtil jwtUtil = new JwtUtil(secret);

    @Test
    @DisplayName("JWT를 생성하고 다시 파싱할 수 있다.")
    void createAndParseToken() {
        Member member = new Member(1L, "브라운", "test@email.com", "ADMIN");

        String token = jwtUtil.createToken(member);
        JwtPayload payload = jwtUtil.parseToken(token);

        assertThat(payload.name()).isEqualTo("브라운");
        assertThat(payload.role()).isEqualTo("ADMIN");
    }
}
