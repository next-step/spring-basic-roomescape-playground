package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;

@Service
public class AuthService {
    private MemberDao memberDao;
    private TokenProvider tokenProvider;
    private AuthorizationExtractor authorizationExtractor;

    public AuthService(MemberDao memberDao, TokenProvider tokenProvider, AuthorizationExtractor authorizationExtractor) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
        this.authorizationExtractor = authorizationExtractor;
    }

    public Member findMember(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        if (member == null) {
            throw new IllegalArgumentException();
        }
        return memberDao.findByEmailAndPassword(email, password);
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = findMember(tokenRequest.getEmail(), tokenRequest.getPassword());

        String accessToken = tokenProvider.createToken(member);

        return new TokenResponse(accessToken);
    }

    public MemberResponse findMemberByToken(String token) {
        Long id = authorizationExtractor.extractMemberId(token);
        Member member = memberDao.findById(id);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
