package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.domain.RefreshToken;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.auth.jwt.JwtTokenProvider;
import roomescape.auth.repository.RefreshTokenDao;
import roomescape.exception.ApplicationException;
import roomescape.member.entity.Member;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenDao refreshTokenDao;
    private final JwtTokenProvider tokenProvider;

    public AuthService(MemberRepository memberRepository, RefreshTokenDao refreshTokenDao, JwtTokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.refreshTokenDao = refreshTokenDao;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.LOGIN_FAILED));

        String accessToken = tokenProvider.createAccessToken(member);
        String refreshToken = tokenProvider.createRefreshToken(member);
        refreshTokenDao.save(new RefreshToken(member.getId(), refreshToken));

        return new TokenResponse(accessToken, refreshToken);
    }

    public String reissue(String refreshToken) {
        refreshTokenDao.findByToken(refreshToken)
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.INVALID_TOKEN));

        Long memberId = tokenProvider.getLoginMemberId(refreshToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));

        return tokenProvider.createAccessToken(member);

    }

    public void logout(Long memberId) {
        refreshTokenDao.deleteByMemberId(memberId);
    }

    public LoginMember findAuthenticatedMember(String token) {
        Long memberId = tokenProvider.getLoginMemberId(token);
        String memberName = tokenProvider.getLoginMemberName(token);
        String memberRole = tokenProvider.getLoginMemberRole(token);

        return new LoginMember(memberId, memberName, memberRole);
    }
}
