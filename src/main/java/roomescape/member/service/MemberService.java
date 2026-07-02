package roomescape.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.member.exception.NoSuchMemberException;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.model.Member;
import roomescape.member.repository.MemberRepository;

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

    public Member loadMemberEntity(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(NoSuchMemberException::new);
    }
}
