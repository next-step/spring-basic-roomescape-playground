package roomescape.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.entity.RefreshTokenEntity;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.auth.jwt.JwtTokenProvider;
import roomescape.auth.repository.RefreshTokenRepository;
import roomescape.exception.ApplicationException;
import roomescape.member.entity.Member;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;

    public AuthService(MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository, JwtTokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.LOGIN_FAILED));

        return createTokens(member);
    }

    @Transactional
    public TokenResponse reissue(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.INVALID_TOKEN));

        Long memberId = tokenProvider.getLoginMemberId(refreshToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));

        return createTokens(member);
    }

    @Transactional
    public void logout(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }

    public LoginMember findAuthenticatedMember(String token) {
        Long memberId = tokenProvider.getLoginMemberId(token);
        String memberName = tokenProvider.getLoginMemberName(token);
        String memberRole = tokenProvider.getLoginMemberRole(token);

        return new LoginMember(memberId, memberName, memberRole);
    }

    private TokenResponse createTokens(Member member) {
        String accessToken = tokenProvider.createAccessToken(member);
        String refreshToken = tokenProvider.createRefreshToken(member);

        refreshTokenRepository.deleteByMemberId(member.getId());
        refreshTokenRepository.save(new RefreshTokenEntity(member, refreshToken));

        return new TokenResponse(accessToken, refreshToken);
    }
}
