package roomescape.application;

import java.lang.module.FindException;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;
import roomescape.token.TokenRequest;
import roomescape.token.TokenResponse;

@Service
public class AuthService {
    private JwtTokenProvider jwtTokenProvider;
    private MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public Member checkInvalidLogin(String principal, String credentials) {
        Member member = null;
        try {
        member = memberDao.findByEmailAndPassword(principal, credentials);
        } catch(AuthorizationException e) {
            e.printStackTrace();
        }
        return member;
    }

    public void findMemberByToken(String token) {
        Long memberId = jwtTokenProvider.getPayload(token);
        System.out.println("mmmmmmmmmmm test : " +memberId);
//        return memberDao.findById(memberId);
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword());
        String accessToken = jwtTokenProvider.createToken(member);
        return new TokenResponse(accessToken);
    }
}
