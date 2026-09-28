package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("no member"));
    }

    public Member findByName(String name) {
        return memberRepository.findByName(name);
    }
}
