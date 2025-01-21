package roomescape.auth;

import org.springframework.stereotype.Service;

import roomescape.auth.jwt.MemberTokenDto;
import roomescape.auth.jwt.TokenService;
import roomescape.member.Member;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Service
public class AuthService {
	private TokenService tokenService;
	private MemberService memberService;

	public AuthService(TokenService tokenService, MemberService memberService) {
		this.tokenService = tokenService;
		this.memberService = memberService;
	}

	public TokenResponse login(LoginRequest loginRequest) {
		String email = loginRequest.email();
		String password = loginRequest.password();

		Member member = memberService.findMemberByEmailAndPassword(email, password);
		if (member == null) {
			throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
		}
		return tokenService.createAccessToken(new MemberTokenDto(member.getId(), member.getName(), member.getEmail()));
	}

	public MemberTokenDto checkLoginStatus(String token) {
		return tokenService.extractMemberResponseFromToken(token);
	}
}
