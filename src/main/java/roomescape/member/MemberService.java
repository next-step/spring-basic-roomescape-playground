package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(),  member.getRole());
    }

    public MemberResponse loadMember(MemberRequest memberRequest) {
        Member member = memberRepository.findByEmailAndPassword(memberRequest.email(), memberRequest.password())
                .orElseThrow(NoSuchMemberException::new);

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public MemberResponse loadMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NoSuchMemberException::new);

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
