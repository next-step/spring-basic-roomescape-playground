package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public LoginMember findLoginMemberByEmail(String email) {
        Member member = memberDao.findByEmail(email);
        if (member == null) {
            throw new AuthorizationException("사용자를 찾을 수 없습니다.");
        }
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public String getEmailFromToken(String token) {
        return jwtTokenProvider.getPayload(token);
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        if (checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }
        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    public boolean checkInvalidLogin(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        if(member == null){
            throw new AuthorizationException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
        return false;
    }

    public boolean verifyToken(String token) {
        try {
            return jwtTokenProvider.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }
}
