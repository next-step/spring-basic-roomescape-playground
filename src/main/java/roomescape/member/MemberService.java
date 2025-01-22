package roomescape.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
