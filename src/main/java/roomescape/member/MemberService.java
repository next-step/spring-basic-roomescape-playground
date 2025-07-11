package roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepo;

    public MemberService(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        if (memberRepo.existsByEmail(memberRequest.getEmail())) {
            throw new IllegalArgumentException("존재하는 이메일입니다.");
        }
        Member member = new Member(
                memberRequest.getName(),
                memberRequest.getEmail(),
                memberRequest.getPassword(),
                "USER"
        );
        Member saved = memberRepo.save(member);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
