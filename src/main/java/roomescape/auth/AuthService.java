package roomescape.auth;

import auth.JwtUtilsV4;
import org.springframework.stereotype.Service;
import roomescape.exception.MemberNotFoundException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtilsV4 jwtUtils;

    public AuthService(MemberRepository memberRepository, JwtUtilsV4 jwtUtils) {
        this.memberRepository = memberRepository;
        this.jwtUtils = jwtUtils;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .orElseThrow(MemberNotFoundException::new);
        return jwtUtils.generateToken(member);
    }

}
