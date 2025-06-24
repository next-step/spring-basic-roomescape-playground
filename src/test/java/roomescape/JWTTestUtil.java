package roomescape;

import org.springframework.stereotype.Component;
import roomescape.auth.JWTUtil;
import roomescape.member.Member;

@Component
public class JWTTestUtil {

    private final JWTUtil jwtUtil;

    public JWTTestUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String createToken(String email, String password) {
        if ("admin@email.com".equals(email) && "password".equals(password)) {
            return jwtUtil.createToken(new Member(1L, "어드민", email, "ADMIN"));
        }
        if ("brown@email.com".equals(email) && "password".equals(password)) {
            return jwtUtil.createToken(new Member(2L, "브라운", email, "USER"));
        }
        throw new IllegalArgumentException("Invalid credentials for test token creation.");
    }
}
