package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenDecoder;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtProvider;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public String login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (member == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return jwtProvider.createToken(member);
    }

    public LoginResponse checkMember(String token) {
        Long memberId = JwtTokenDecoder.decodeToken(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member"));
        return new LoginResponse(member.getName());
    }

    public Member getMemberFromToken(String token) {
        Long memberId = JwtTokenDecoder.decodeToken(token);
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member"));
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}