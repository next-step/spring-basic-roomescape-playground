package roomescape.auth;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.member.JwtProvider;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.dto.AuthUserNameResponse;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.LoginResponse;
import roomescape.member.dto.MemberResponse;

@Service
public class AuthService {

	private final JwtProvider jwtProvider;
	private final MemberRepository memberRepository;

	public AuthService(JwtProvider jwtProvider, MemberRepository memberRepository) {
		this.jwtProvider = jwtProvider;
		this.memberRepository = memberRepository;
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest loginRequest) {
		MemberResponse memberResponse = authenticate(loginRequest.email(), loginRequest.password());
		return new LoginResponse(jwtProvider.generateToken(memberResponse));
	}

	private MemberResponse authenticate(String email, String password) {
		Member member = memberRepository.findByEmailAndPassword(email, password)
			.orElseThrow(() -> new NoSuchElementException("Member not found"));
		return toMemberResponse(member);
	}

	@Transactional(readOnly = true)
	public AuthUserNameResponse findNameByToken(String token) {
		LoginMember loginMember = jwtProvider.parseLoginMemberFromToken(token);

		Member member = memberRepository.findById(loginMember.id())
			.orElseThrow(() -> new IllegalArgumentException("Member not found"));

		return new AuthUserNameResponse(member.getName());
	}

	public LoginMember getLoginMemberFromToken(String token) {
		return jwtProvider.parseLoginMemberFromToken(token);
	}

	private MemberResponse toMemberResponse(Member member) {
		return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
	}

}
