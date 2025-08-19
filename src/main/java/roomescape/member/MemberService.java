package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = new Member(
            memberRequest.getName(),
            memberRequest.getEmail(),
            memberRequest.getPassword(),
            "USER"
        );

        memberRepository.save(member);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
