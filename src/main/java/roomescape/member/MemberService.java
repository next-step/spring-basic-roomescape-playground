package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import auth.JwtUtils;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtUtils jwtUtils) {
        this.memberRepository = memberRepository;
        this.jwtUtils = jwtUtils;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    //email 이랑 password받아서 토큰 생성 후 리턴
    public String login(@RequestBody LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        return jwtUtils.createToken(member);
    }

    public MemberCheckResponse memberChecking(String token) throws IllegalAccessException {
        TokenResponse tokenResponse = jwtUtils.verifyToken(token);
        Member member = memberRepository.findByName(tokenResponse.getName());
        if (member == null) {
            throw new IllegalAccessException("Invalid");
        }
        return new MemberCheckResponse(member.getName());
    }

    public Member findByName(String name) {
        return memberRepository.findByName(name);
    }
}
