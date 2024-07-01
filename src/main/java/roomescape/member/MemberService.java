package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import roomescape.jwt.JwtProvider;
import roomescape.jwt.JwtUtil;

@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;

	@Autowired
	public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
		this.memberRepository = memberRepository;
		this.jwtProvider = jwtProvider;
	}

	public String login(MemberLoginRequest request) {
		Member member = memberRepository.findByEmailAndPassword(request.email(), request.password());
		if (member == null) {
			throw new IllegalArgumentException("Invalid email or password");
		}
		return jwtProvider.createToken(member);
	}

	public MemberCheckResponse checkMember(String token) {
		Long memberId = JwtUtil.getIdFromToken(token);
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid member"));
		return new MemberCheckResponse(member.getName());
	}

	public Member getMemberFromToken(String token) {
		Long memberId = JwtUtil.getIdFromToken(token);
		return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member"));
	}

	public MemberResponse createMember(MemberRequest memberRequest) {
		Member member = memberRepository.save(
			new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
		return new MemberResponse(member.getId(), member.getName(), member.getEmail());
	}
}
