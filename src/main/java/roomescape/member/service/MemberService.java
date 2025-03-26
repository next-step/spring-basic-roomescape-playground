package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.repository.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.request.MemberRequest;
import roomescape.member.dto.response.MemberResponse;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), Role.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
