package roomescape.login;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.jwt.JwtUtil;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public LoginService(JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    public String authenticate(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password).get();
        if (member == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return jwtUtil.generateToken(member.getId(), member.getRole());
    }

    public Cookie createAuthCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Map<String, Object> createLoginResponse(String token) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("message", "Login successful");
        return responseBody;
    }
}
