package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.util.JwtPayload;
import roomescape.auth.util.JwtUtil;
import roomescape.member.Member;
import roomescape.member.MemberInfo;
import roomescape.member.MemberRepository;


@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public LoginService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
            .orElseThrow(
                () -> new IllegalArgumentException("not found Member with email: " + email));
        return jwtUtil.createToken(member);
    }

    public MemberInfo check(String token) {
        JwtPayload payload = jwtUtil.parseToken(token);
        Member member = memberRepository.findByName(payload.name())
            .orElseThrow(() -> new IllegalArgumentException(
                "not found Member with name: " + payload.name()));

        return new MemberInfo(
            member.getName(),
            member.getRole()
        );
    }
}
