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
		Member member = memberRepository.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
		return new MemberResponseDto(member.getId(), member.getName(), member.getEmail());
    }

    public Member login(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password).orElseThrow();
    }
}
