package roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TokenUtil;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;
    private TokenUtil tokenUtil;

    public MemberService(MemberRepository memberRepository, TokenUtil tokenUtil) {
        this.memberRepository = memberRepository;
        this.tokenUtil = tokenUtil;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), Role.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .orElseThrow(() -> new AuthorizationException("이메일 또는 비밀번호가 일치하지 않습니다."));
        return tokenUtil.createToken(member);
    }

    public Member findMemberByToken(String token) {
        Long memberId = tokenUtil.getMemberId(token);

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthorizationException("존재하지 않는 회원입니다."));
    }
}
