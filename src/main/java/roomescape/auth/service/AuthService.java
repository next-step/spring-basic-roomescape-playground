package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.security.JwtTokenProvider;
import roomescape.auth.dto.LoginResponse;
import roomescape.member.dto.Member;
import roomescape.member.dao.MemberDao;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public LoginResponse createToken(String email) {
        Member member = memberDao.findByEmail(email);
        String token = jwtTokenProvider.createToken(member.getEmail());
        return new LoginResponse(token);
    }

    public Member getLoginMember(String token) {
        String userEmail = jwtTokenProvider.getEmailFromToken(token);
        return memberDao.findByEmail(userEmail);
    }

    public boolean isTokenInvalid(String accessToken) {
        return jwtTokenProvider.isTokenInvalid(accessToken);
    }
}
