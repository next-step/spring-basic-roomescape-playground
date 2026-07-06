package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.auth.jwt.JwtTokenProvider;
import roomescape.exception.ApplicationException;
import roomescape.member.domain.Member;
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
        Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        return tokenProvider.createAccessToken(member);
    }

    public LoginMember findAuthenticatedMember(String token) {
        Long memberId = tokenProvider.getLoginMemberId(token);
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS));
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole().name());
    }
}
