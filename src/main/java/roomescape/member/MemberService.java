package roomescape.member;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import roomescape.exception.MemberNotFoundException;

@Service
@Transactional
public class MemberService {
    
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member getMemberById(Long id) {
        Member member = memberRepository.findById(id);
        if(member == null) {
            throw new MemberNotFoundException();
        }
        return member;
    }

}
