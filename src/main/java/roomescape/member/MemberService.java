package roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponseDto createMember(MemberRequestDto memberRequest) {
        Member memberToSave = new Member(
                memberRequest.name(),
                memberRequest.email(),
                memberRequest.password(),
                Role.USER
        );
        Member savedMember = memberRepository.save(memberToSave);
        return MemberResponseDto.from(savedMember);
    }

    public Member login(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password).orElseThrow();
    }
}
