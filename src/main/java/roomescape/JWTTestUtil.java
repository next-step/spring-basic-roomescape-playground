package roomescape;

import roomescape.member.Member;

public class JWTTestUtil {

    public static String createToken(String email, String password) {
        if ("admin@email.com".equals(email) && "password".equals(password)) {
            return JWTUtil.createToken(new Member(1L, "어드민", email, "ADMIN"));
        }
        if ("brown@email.com".equals(email) && "password".equals(password)) {
            return JWTUtil.createToken(new Member(2L, "브라운", email, "USER"));
        }
        throw new IllegalArgumentException("Invalid credentials for test token creation.");
    }
}
