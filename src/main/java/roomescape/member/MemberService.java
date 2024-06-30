package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import roomescape.jwt.JwtProvider;
import roomescape.jwt.JwtUtil;

@Service
public class MemberService {
	private MemberDao memberDao;
	private JwtProvider jwtProvider;

	@Autowired
	public MemberService(MemberDao memberDao, JwtProvider jwtProvider) {
		this.memberDao = memberDao;
		this.jwtProvider = jwtProvider;
	}

	public String login(MemberLoginRequest request) {
		Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
		if (member == null) {
			throw new IllegalArgumentException("Invalid email or password");
		}
		return jwtProvider.createToken(member);
	}

	public MemberCheckResponse checkMember(String token) {
		Long memberId = JwtUtil.getIdFromToken(token);
		Member member = memberDao.findById(memberId);
		if (member == null) {
			throw new IllegalArgumentException("Invalid member");
		}
		return new MemberCheckResponse(member.getName());
	}

	public Member getMemberFromToken(String token) {
		Long memberId = JwtUtil.getIdFromToken(token);
		return memberDao.findById(memberId);
	}

	public MemberResponse createMember(MemberRequest memberRequest) {
		Member member = memberDao.save(
			new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
		return new MemberResponse(member.getId(), member.getName(), member.getEmail());
	}
}
