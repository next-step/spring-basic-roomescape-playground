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

		Member member = memberRepository.save(Member.ofUser(name, email, password));
		return toMemberResponse(member);
	}

	private MemberResponse toMemberResponse(Member member) {
		return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
	}

}
