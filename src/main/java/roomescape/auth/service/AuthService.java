package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.jwt.JwtTokenProvider;
import roomescape.exception.ApplicationException;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.repository.MemberDao;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider tokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.LOGIN_FAILED));
        return tokenProvider.createAccessToken(member);
    }

    public LoginMember findAuthenticatedMember(String token) {
        Long memberId = tokenProvider.getLoginMemberId(token);
        String memberName = tokenProvider.getLoginMemberName(token);
        String memberRole = tokenProvider.getLoginMemberRole(token);

        return new LoginMember(memberId, memberName, memberRole);
    }
}
