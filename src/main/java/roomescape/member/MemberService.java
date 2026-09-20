package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.exception.AuthenticationException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new AuthenticationException("이메일 또는 비밀번호가 일치하지 않습니다."));
        return jwtTokenProvider.createToken(member);
    }

    public Member findMemberByToken(String token) {
        long id = jwtTokenProvider.getMemberId(token);
        return memberRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 회원입니다."));
    }
}
