package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(
                        memberRequest.getName(),
                        memberRequest.getEmail(),
                        memberRequest.getPassword(),
                        "USER"
                )
        );

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member login(LoginRequest loginRequest) {
        return memberRepository.findByEmailAndPassword(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ).orElseThrow();
    }

    public LoginCheckResponse checkLogin(LoginMember loginMember) {
        return new LoginCheckResponse(loginMember.getName());
    }

    public LoginMember findLoginMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        return new LoginMember(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }
}
