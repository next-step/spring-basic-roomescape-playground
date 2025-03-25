package roomescape.login;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.util.JwtUtil;

@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public LoginService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password);
        return jwtUtil.generateToken(member);
    }

    public LoginCheckResponse getUserInfoFromToken(String token) {
        Claims claims = jwtUtil.parseClaims(token);

        String memberName = claims.get("name", String.class);
        Member member = memberRepository.findByName(memberName);
        return new LoginCheckResponse(member.getName());
    }
}
