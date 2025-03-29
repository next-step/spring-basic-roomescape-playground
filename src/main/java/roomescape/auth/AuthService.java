package roomescape.auth;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.member.JwtProvider;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.dto.AuthUserNameResponse;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.LoginResponse;
import roomescape.member.dto.MemberResponse;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberDao memberDao;

    public AuthService(JwtProvider jwtProvider, MemberDao memberDao) {
        this.jwtProvider = jwtProvider;
        this.memberDao = memberDao;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        MemberResponse memberResponse = authenticate(loginRequest.email(), loginRequest.password());
        return new LoginResponse(jwtProvider.generateToken(memberResponse));
    }

    private MemberResponse authenticate(String email, String password) {
        Member member = getMemberByEmailAndPassword(email, password);
        return toMemberResponse(member);
    }

    private Member getMemberByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
    }

    public AuthUserNameResponse findNameByToken(String token) {
        LoginMember loginMember = jwtProvider.parseLoginMemberFromToken(token);

        Member member = getMemberById(loginMember.id());

        return new AuthUserNameResponse(member.getName());
    }

    public LoginMember getLoginMemberFromToken(String token) {
        return jwtProvider.parseLoginMemberFromToken(token);
    }

    private Member getMemberById(Long memberId) {
        return memberDao.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    private MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

}
