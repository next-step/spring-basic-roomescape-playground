package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.MemberAuthContext;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberAuthContext loginByEmailAndPassword(MemberLoginRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                                        .orElseThrow(() -> new IllegalArgumentException("로그인 정보가 불일치 합니다."));
        return new MemberAuthContext(member.getName(), member.getRole());
    }

    public MemberResponse checkLogin(MemberAuthContext authContext) {
        Member member = memberRepository.findByName(authContext.name())
                                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
