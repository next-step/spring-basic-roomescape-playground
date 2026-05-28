package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginResponse;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberAuthInfo;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse createToken(MemberAuthInfo memberAuthInfo) {
        String accessToken = jwtTokenProvider.createToken(
                memberAuthInfo.memberId(),
                memberAuthInfo.name(),
                memberAuthInfo.stringRole());

        return new LoginResponse(accessToken);
    }

    public Long getMemberId(String token) {
        return jwtTokenProvider.findMemberId(token);
    }

    public Member getMember(String token) {
        return jwtTokenProvider.findMember(token);
    }
}
