package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.member.LoginResponse;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private  final MemberDao memberDao;

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




}
