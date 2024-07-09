package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import roomescape.auth.JwtUtils;

@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtUtils jwtUtils;

	@Autowired
	public MemberService(MemberRepository memberRepository, JwtUtils jwtUtils) {
		this.memberRepository = memberRepository;
		this.jwtUtils = jwtUtils;
	}

	public String login(MemberLoginRequest request) {
		Member member = memberRepository.getByEmailAndPassword(request.email(), request.password());
		return jwtUtils.createToken(member);
	}

	public MemberCheckResponse checkMember(String token) {
		Long memberId = jwtUtils.getIdFromToken(token);
		Member member = memberRepository.getById(memberId);
		return new MemberCheckResponse(member.getName());
	}

	public Member getMemberFromToken(String token) {
		Long memberId = jwtUtils.getIdFromToken(token);
		return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member"));
	}

	public MemberResponse createMember(MemberRequest memberRequest) {
		Member member = memberRepository.save(
			new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
		return new MemberResponse(member.getId(), member.getName(), member.getEmail());
	}
}
