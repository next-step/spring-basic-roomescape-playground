package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        String name = memberRequest.name();
        String email = memberRequest.email();
        String password = memberRequest.password();

        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = memberRepository.save(Member.createUser(name, email, password));
        return toMemberResponse(member);
    }

    private MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

}
