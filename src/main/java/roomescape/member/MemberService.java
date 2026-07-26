package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = Member.builder()
                        .name(memberRequest.name())
                                .email(memberRequest.email())
                                        .password(memberRequest.password())
                                                .role("USER")
                                                        .build();
        memberRepository.save(member);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
